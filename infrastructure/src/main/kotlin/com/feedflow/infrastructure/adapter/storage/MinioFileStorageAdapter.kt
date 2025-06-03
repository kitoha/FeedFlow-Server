package com.feedflow.infrastructure.adapter.storage

import com.feedflow.application.dto.stroage.PresignedUploadCommand
import com.feedflow.domain.model.storage.PresignedResponse
import com.feedflow.application.dto.stroage.UploadFileCommand
import com.feedflow.application.port.storage.FileStoragePort
import com.feedflow.domain.exception.FileStorageException
import com.feedflow.infrastructure.config.MinioProperties
import com.feedflow.infrastructure.logging.log
import io.minio.*
import io.minio.errors.MinioException
import io.minio.http.Method
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.concurrent.TimeUnit

@Service
class MinioFileStorageAdapter(
  private val minioClient: MinioClient,
  private val minioProperties: MinioProperties
) : FileStoragePort {

  @PostConstruct
  fun initializeBuckets() {
    minioProperties.buckets.forEach { (category, bucketName) ->
      try {
        if (ensureBucketExists(bucketName)) {
          log.info { "Bucket '$bucketName' created." }
        } else {
          log.info { "Bucket '$bucketName' already exists." }
        }
      } catch (e: Exception) {
        log.error(e) { "Failed to prepare bucket '$bucketName'" }
        throw IllegalStateException("Failed to initialize MinIO buckets", e)
      }
    }
  }

  override fun uploadFile(file: UploadFileCommand, bucketName: String): Boolean {
    try {
      val putObjectArgs = PutObjectArgs.builder()
        .bucket(bucketName)
        .`object`(file.filename)
        .stream(file.inputStream, file.size, -1)
        .contentType(file.contentType)
        .build()
      minioClient.putObject(putObjectArgs)
      return true
    } catch (e: Exception) {
      throw FileStorageException.FileUploadException(file.filename, e)
    }
  }

  override fun downloadFile(fileName: String, bucketName: String): ByteArray {
    try {
      val getObjectArgs = GetObjectArgs.builder()
        .bucket(bucketName)
        .`object`(fileName)
        .build()
      val stream = minioClient.getObject(getObjectArgs)

      return stream.readAllBytes()
    } catch (e: MinioException) {
      log.error(e) { "MinIO client error downloading file '$fileName' from bucket '$bucketName': ${e.message}" }
      throw FileStorageException.FileDownloadException(fileName, e)
    } catch (e: Exception) {
      log.error(e) { "Unexpected error downloading file '$fileName' from bucket '$bucketName': ${e.message}" }
      throw FileStorageException.FileDownloadException(fileName, e)
    }
  }

  override fun generateUploadPresignedUrl(
    presignedUploadCommand: PresignedUploadCommand
  ): PresignedResponse {
    try {
      val fileKey = "${UUID.randomUUID()}_${presignedUploadCommand.fileName}"

      val presignedObjectUrlArgs = GetPresignedObjectUrlArgs.builder()
        .method(Method.PUT)
        .bucket(presignedUploadCommand.bucketName)
        .`object`(fileKey)
        .expiry(15, TimeUnit.MINUTES)
        .extraHeaders(mapOf("Content-Type" to presignedUploadCommand.fileType))
        .build()

      val presignedUrl = minioClient.getPresignedObjectUrl(presignedObjectUrlArgs)

      return PresignedResponse(fileName = fileKey, fileUrl = presignedUrl)
    } catch (e: Exception) {
      log.error(e) { "Failed to generate presigned URL for '${presignedUploadCommand.fileName}' in bucket '${presignedUploadCommand.bucketName}'" }
      throw FileStorageException.PreSignedUrlException(presignedUploadCommand.fileName, e)
    }
  }

  override fun generateDownloadPresignedUrl(fileKey: String, bucketName: String): String {
    try {
      val presignedObjectUrlArgs = GetPresignedObjectUrlArgs.builder()
        .method(Method.GET)
        .bucket(bucketName)
        .`object`(fileKey)
        .expiry(15, TimeUnit.MINUTES)
        .build()

      val presignedUrl = minioClient.getPresignedObjectUrl(presignedObjectUrlArgs)

      return presignedUrl
    } catch (e: Exception) {
      log.error(e) { "Failed to generate download presigned URL for '$fileKey'" }
      throw FileStorageException.PreSignedUrlException(fileKey, e)
    }
  }

  private fun ensureBucketExists(buketName: String): Boolean {
    val bucketExistsArgs = BucketExistsArgs.builder().bucket(buketName).build()
    val isBucketExists = minioClient.bucketExists(bucketExistsArgs)
    if (isBucketExists) {
      return false
    } else {
      val makeBucketArgs = MakeBucketArgs.builder().bucket(buketName).build()
      minioClient.makeBucket(makeBucketArgs)
      return true
    }
  }
}
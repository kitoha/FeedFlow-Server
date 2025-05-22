package com.feedflow.infrastructure.adapter.storage

import com.feedflow.domain.model.storage.MinioFileResponse
import com.feedflow.application.dto.stroage.UploadFileCommand
import com.feedflow.application.port.storage.FileStoragePort
import com.feedflow.domain.enums.storage.StorageMethod
import com.feedflow.domain.exception.FileStorageException
import com.feedflow.infrastructure.config.MinioProperties
import com.feedflow.infrastructure.logging.log
import io.minio.*
import io.minio.errors.MinioException
import io.minio.http.Method
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
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

  override fun generatePreSignUrl(
    fileName: String,
    bucketName: String,
    method: StorageMethod
  ): MinioFileResponse {
    try {
      val minioMethod = Method.valueOf(method.toString())
      val presignedObjectUrlArgs = GetPresignedObjectUrlArgs.builder()
        .method(minioMethod)
        .bucket(bucketName)
        .`object`(fileName)
        .expiry(15, TimeUnit.MINUTES)
        .build()

      val presignedUrl = minioClient.getPresignedObjectUrl(presignedObjectUrlArgs)

      return MinioFileResponse(fileName = fileName, fileUrl = presignedUrl)
    } catch (e: Exception) {
      log.error(e) { "Failed to generate presigned URL for '$fileName' in bucket '$bucketName'" }
      throw FileStorageException.PreSignedUrlException(fileName, e)
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
package com.feedflow.application.service.storage

import com.feedflow.application.dto.stroage.PresignedUploadCommand
import com.feedflow.application.dto.stroage.UploadFileCommand
import com.feedflow.application.port.storage.FileStoragePort
import com.feedflow.domain.model.storage.PresignedResponse
import org.springframework.stereotype.Service

@Service
class FileService(
  private val fileStoragePort: FileStoragePort
) {

  fun upload(uploadFileCommand: UploadFileCommand, bucketName: String): Boolean {
    return fileStoragePort.uploadFile(uploadFileCommand, bucketName)
  }

  fun download(fileName: String, bucketName: String): ByteArray {
    return fileStoragePort.downloadFile(fileName, bucketName)
  }

  fun generatePreSignUrl(presignedUploadCommand: PresignedUploadCommand): PresignedResponse {
    return fileStoragePort.generateUploadPresignedUrl(presignedUploadCommand)
  }
}
package com.feedflow.application.service.storage

import com.feedflow.application.dto.stroage.UploadFileCommand
import com.feedflow.application.port.storage.FileStoragePort
import com.feedflow.domain.enums.storage.StorageMethod
import com.feedflow.domain.model.storage.MinioFileResponse
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

  fun generatePreSignUrl(fileName: String, bucketName: String): MinioFileResponse {
    return fileStoragePort.generatePreSignUrl(fileName, bucketName, StorageMethod.GET)
  }
}
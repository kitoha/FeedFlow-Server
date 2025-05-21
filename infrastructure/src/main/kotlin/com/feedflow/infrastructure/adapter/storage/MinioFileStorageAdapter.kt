package com.feedflow.infrastructure.adapter.storage

import com.feedflow.application.dto.stroage.UploadFileCommand
import com.feedflow.application.port.storage.FileStoragePort

class MinioFileStorageAdapter : FileStoragePort{

  override fun uploadFile(file: UploadFileCommand, bucketName: String): Boolean {
    TODO("Not yet implemented")
  }

  override fun downloadFile(fileName: UploadFileCommand, bucketName: String): ByteArray {
    TODO("Not yet implemented")
  }
}
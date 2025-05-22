package com.feedflow.application.port.storage

import com.feedflow.application.dto.stroage.UploadFileCommand
import com.feedflow.domain.enums.storage.StorageMethod
import com.feedflow.domain.model.storage.MinioFileResponse

interface FileStoragePort {
  fun uploadFile(file: UploadFileCommand, bucketName: String): Boolean

  fun downloadFile(fileName: String, bucketName: String): ByteArray

  fun generatePreSignUrl(fileName: String, bucketName: String, method: StorageMethod ) : MinioFileResponse
}
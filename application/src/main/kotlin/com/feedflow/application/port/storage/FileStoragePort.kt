package com.feedflow.application.port.storage

import com.feedflow.application.dto.stroage.PresignedUploadCommand
import com.feedflow.application.dto.stroage.UploadFileCommand
import com.feedflow.domain.model.storage.PresignedResponse

interface FileStoragePort {
  fun uploadFile(file: UploadFileCommand, bucketName: String): Boolean

  fun downloadFile(fileName: String, bucketName: String): ByteArray

  fun generateUploadPresignedUrl(presignedUploadCommand: PresignedUploadCommand) : PresignedResponse

  fun generateDownloadPresignedUrl(fileKey: String, bucketName: String) : String
}
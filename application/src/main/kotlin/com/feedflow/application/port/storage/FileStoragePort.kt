package com.feedflow.application.port.storage

import com.feedflow.application.dto.stroage.UploadFileCommand
import org.springframework.web.multipart.MultipartFile

interface FileStoragePort {
  fun uploadFile(file: UploadFileCommand, bucketName: String): Boolean

  fun downloadFile(fileName: UploadFileCommand, bucketName: String): ByteArray
}
package com.feedflow.api.validator

import com.feedflow.application.dto.stroage.UploadFileCommand
import com.feedflow.domain.exception.FileStorageException
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class SimpleMultipartFileValidator {

  fun toCommand(file: MultipartFile): UploadFileCommand {
    val name = file.originalFilename
      ?.takeIf { it.isNotBlank() }
      ?: throw FileStorageException.InvalidFileException("filename required")

    val type = file.contentType
      ?: throw FileStorageException.InvalidFileException("content-type required")

    return UploadFileCommand(
      filename = name,
      contentType = type,
      size = file.size,
      inputStream = file.inputStream
    )
  }
}

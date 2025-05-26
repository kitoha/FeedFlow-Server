package com.feedflow.api.request

import com.feedflow.application.dto.stroage.PresignedUploadCommand

data class PresignedUrlRequest(
  val fileName: String,
  val bucketName: String,
  val fileType: String,
  val fileSize: Long
){
  fun toCommand(): PresignedUploadCommand{
    return PresignedUploadCommand(
      fileName = fileName,
      bucketName = bucketName,
      fileType = fileType,
      fileSize = fileSize
    )
  }
}

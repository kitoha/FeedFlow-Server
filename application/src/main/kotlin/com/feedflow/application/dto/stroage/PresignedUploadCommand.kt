package com.feedflow.application.dto.stroage

data class PresignedUploadCommand(
  val fileName: String,
  val bucketName: String,
  val fileType: String,
  val fileSize: Long
)

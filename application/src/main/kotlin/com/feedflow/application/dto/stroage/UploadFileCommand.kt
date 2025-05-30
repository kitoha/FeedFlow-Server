package com.feedflow.application.dto.stroage

import java.io.InputStream

data class UploadFileCommand(
  val filename: String,
  val contentType: String,
  val size: Long,
  val inputStream: InputStream
)
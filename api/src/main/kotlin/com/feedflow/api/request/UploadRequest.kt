package com.feedflow.api.request

import org.springframework.web.multipart.MultipartFile

data class UploadRequest(
  val file : MultipartFile,
  val bucketName : String
)

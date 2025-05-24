package com.feedflow.api.request

import org.springframework.web.multipart.MultipartFile

class UploadRequest {
  lateinit var file: MultipartFile
  lateinit var bucketName: String
}

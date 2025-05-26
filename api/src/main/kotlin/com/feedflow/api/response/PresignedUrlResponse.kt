package com.feedflow.api.response

data class PresignedUrlResponse(
  val uploadUrl: String,
  val fileKey: String,
  val expiresIn: Long
)

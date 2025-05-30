package com.feedflow.application.dto.post

data class PostResponse(
  val id: String,
  val authorId: String,
  val content: String,
  val fileUrls: List<String>,
  val visibility: String,
  val createdAt: String,
  val updatedAt: String,
  val deletedAt: String?
)
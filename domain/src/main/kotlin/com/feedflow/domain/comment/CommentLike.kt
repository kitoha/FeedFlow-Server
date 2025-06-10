package com.feedflow.domain.comment

import java.time.LocalDateTime

data class CommentLike(
  val commentId: String,
  val userId: String,
  val createdAt: LocalDateTime? = null
)

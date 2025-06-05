package com.feedflow.api.response

import com.feedflow.domain.comment.Comment

data class CommentResponse(
  val id: String,
  val contents: String,
  val authorId: String,
  val likeCount: Long,
  val replyCount: Long,
  val createdAt: String,
  val updatedAt: String
) {
  companion object {
    fun from(comment: Comment): CommentResponse {
      return CommentResponse(
        id = comment.id,
        contents = comment.contents,
        authorId = comment.authorId,
        likeCount = comment.likeCount,
        replyCount = comment.replyCount,
        createdAt = comment.createdAt.toString(),
        updatedAt = comment.updatedAt.toString()
      )
    }
  }
}

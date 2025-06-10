package com.feedflow.api.request

import com.feedflow.domain.comment.Comment

data class CreateCommentRequest(
  val contents: String,
  val postId: String,
  val authorId: String,
  val parentId: String? = null
)
{
  fun toComment(userId: String) = Comment.create(
    contents = contents,
    postId = postId,
    authorId = userId,
    parentId = parentId
  )
}
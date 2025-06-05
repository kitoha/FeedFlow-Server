package com.feedflow.domain.port.comment

import com.feedflow.domain.comment.CommentLike

interface CommentLikeRepository {
  fun save(commentLike: CommentLike): CommentLike
  fun existsByCommentIdAndUserId(commentId: String, userId: String): Boolean
  fun deleteByCommentIdAndUserId(commentId: String, userId: String): Boolean
  fun findByCommentId(commentId: String): List<CommentLike>
  fun findByUserId(userId: String): List<CommentLike>
  fun countByCommentId(commentId: String): Long
}
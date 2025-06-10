package com.feedflow.domain.port.comment

import com.feedflow.domain.comment.Comment
import com.feedflow.domain.model.Page
import com.feedflow.domain.model.Pageable

interface CommentRepository {
  fun save(comment: Comment): Comment
  fun findById(commentId: String): Comment?
  fun findByPostId(postId: String, pageable: Pageable): Page<Comment>
  fun findRepliesByParentId(parentId: String, pageable: Pageable): Page<Comment>
  fun deleteById(commentId: String)
  fun incrementLikeCount(commentId: String): Long
  fun decrementLikeCount(commentId: String): Long
  fun updateReplyCount(parentId: String, increment: Boolean)
  fun existsByIdAndAuthorId(commentId: String, authorId: String): Boolean
}
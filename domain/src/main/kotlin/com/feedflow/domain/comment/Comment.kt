package com.feedflow.domain.comment

import com.feedflow.domain.enums.comment.CommentStatus
import com.feedflow.domain.utils.Tsid
import java.time.LocalDateTime

data class Comment(
  val id: String,
  val contents: String,
  val postId: String,
  val authorId: String,
  val parentId: String? = null,
  val children: List<Comment> = emptyList(),
  val status: CommentStatus = CommentStatus.ACTIVE,
  val likeCount: Long = 0,
  val replyCount: Long = 0,
  val createdAt: LocalDateTime,
  val updatedAt: LocalDateTime
) {

  fun isRoot(): Boolean = parentId == null
  fun isActive(): Boolean = status == CommentStatus.ACTIVE

  companion object {

    fun create(
      contents: String,
      authorId: String?,
      postId: String,
      parentId: String? = null
    ): Comment {
      return of(
        id = Tsid.generate(),
        authorId = authorId,
        contents = contents,
        postId = postId,
        parentId = parentId,
        commentStatus = CommentStatus.ACTIVE
      )
    }

    private fun of(
      id: String,
      authorId: String?,
      contents: String,
      postId: String,
      parentId: String?,
      commentStatus: CommentStatus
    ): Comment {
      require(contents.isNotBlank()) { "댓글 내용은 필수입니다." }
      require(contents.length <= 1000) { "댓글은 1000자를 초과할 수 없습니다." }
      require(authorId != null) { "작성자는 필수입니다." }

      val now = LocalDateTime.now()
      return Comment(
        id = id,
        contents = contents,
        postId = postId,
        authorId = authorId,
        parentId = parentId,
        status = commentStatus,
        createdAt = now,
        updatedAt = now
      )
    }
  }
}
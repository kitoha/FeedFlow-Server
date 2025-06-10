package com.feedflow.infrastructure.entity.comment

import com.feedflow.domain.enums.comment.CommentStatus
import com.feedflow.infrastructure.entity.common.AuditEntity
import jakarta.persistence.*

@Entity
@Table(name = "comment")
class CommentEntity(
  @Id
  val id: Long = 0,

  @Column(nullable = false, length = 1000)
  var contents: String,

  @Column(name = "post_id", nullable = false)
  val postId: Long,

  @Column(name = "author_id", nullable = false)
  val authorId: Long,

  @Column(name = "parent_id")
  val parentId: String? = null,

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  var status: CommentStatus = CommentStatus.ACTIVE,

  @Column(name = "like_count", nullable = false)
  var likeCount: Long = 0,

  @Column(name = "reply_count", nullable = false)
  var replyCount: Long = 0,

  @Column(nullable = false)
  val depth: Int = 0,

  @Version
  var version: Long = 0
): AuditEntity() {
}
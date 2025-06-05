package com.feedflow.infrastructure.repository.comment

import com.feedflow.infrastructure.entity.comment.CommentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface CommentJpaRepository : JpaRepository<CommentEntity, Long> {
  @Modifying
  @Query("UPDATE CommentJpaEntity c SET c.likeCount = c.likeCount + 1 WHERE c.id = :id")
  fun incrementLikeCount(id: Long): Int

  @Modifying
  @Query("UPDATE CommentJpaEntity c SET c.likeCount = c.likeCount - 1 WHERE c.id = :id AND c.likeCount > 0")
  fun decrementLikeCount(id: Long): Int

  @Modifying
  @Query("UPDATE CommentJpaEntity c SET c.replyCount = c.replyCount + :increment WHERE c.id = :parentId")
  fun updateReplyCount(parentId: Long, increment: Long): Int

  fun existsByIdAndAuthorId(id: Long, authorId: String): Boolean
}
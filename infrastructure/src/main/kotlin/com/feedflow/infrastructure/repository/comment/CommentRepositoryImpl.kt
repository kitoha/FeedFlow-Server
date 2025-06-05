package com.feedflow.infrastructure.repository.comment

import com.feedflow.domain.comment.Comment
import com.feedflow.domain.enums.comment.CommentStatus
import com.feedflow.domain.model.Page
import com.feedflow.domain.model.Pageable
import com.feedflow.domain.port.comment.CommentRepository
import com.feedflow.domain.utils.Tsid
import com.feedflow.infrastructure.entity.comment.CommentEntity
import com.feedflow.infrastructure.entity.comment.QCommentEntity
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class CommentRepositoryImpl(
  private val commentJpaRepository: CommentJpaRepository,
  private val queryFactory: JPAQueryFactory
) : CommentRepository {

  override fun save(comment: Comment): Comment {
    val entity = CommentEntity(
      contents = comment.contents,
      postId = Tsid.decode(comment.postId),
      authorId = Tsid.decode(comment.authorId),
      parentId = comment.parentId,
      status = comment.status,
      likeCount = comment.likeCount,
      replyCount = comment.replyCount
    )

    val savedEntity = commentJpaRepository.save(entity)
    return convertToDomain(savedEntity)
  }

  override fun findById(commentId: String): Comment? {
    return commentJpaRepository.findById(commentId.toLong())
      .map { convertToDomain(it) }
      .orElse(null)
  }

  override fun findByPostId(postId: String, pageable: Pageable): Page<Comment> {
    val qComment = QCommentEntity.commentEntity

    val comments = queryFactory
      .selectFrom(qComment)
      .where(
        qComment.postId.eq(postId)
          .and(qComment.parentId.isNull) // 최상위 댓글만
          .and(qComment.status.eq(CommentStatus.ACTIVE))
      )
      .orderBy(qComment.createdAt.desc())
      .offset(pageable.offset())
      .limit(pageable.size.toLong())
      .fetch()

    val total = queryFactory
      .select(qComment.count())
      .from(qComment)
      .where(
        qComment.postId.eq(postId)
          .and(qComment.parentId.isNull)
          .and(qComment.status.eq(CommentStatus.ACTIVE))
      )
      .fetchOne() ?: 0L

    if (total == 0L) {
      return Page.empty(pageable)
    }

    val domainComments = comments.map { convertToDomain(it) }
    val totalPages = ((total - 1) / pageable.size) + 1

    return Page(
      content = domainComments,
      pageable = pageable,
      totalElements = total,
      totalPages = totalPages.toInt(),
      isLast = (pageable.page + 1) * pageable.size >= total,
      isFirst = pageable.page == 0,
      isEmpty = domainComments.isEmpty()
    )
  }

  override fun findRepliesByParentId(parentId: String, pageable: Pageable): Page<Comment> {
    val qComment = QCommentEntity.commentEntity

    val replies = queryFactory
      .selectFrom(qComment)
      .where(
        qComment.parentId.eq(parentId)
          .and(qComment.status.eq(CommentStatus.ACTIVE))
      )
      .orderBy(qComment.createdAt.asc())
      .offset(pageable.offset())
      .limit(pageable.size.toLong())
      .fetch()

    val total = queryFactory
      .select(qComment.count())
      .from(qComment)
      .where(
        qComment.parentId.eq(parentId)
          .and(qComment.status.eq(CommentStatus.ACTIVE))
      )
      .fetchOne() ?: 0L

    val domainComments = replies.map { convertToDomain(it) }
    val totalPages = ((total - 1) / pageable.size) + 1

    return Page(
      content = domainComments,
      pageable = pageable,
      totalElements = total,
      totalPages = totalPages.toInt(),
      isLast = (pageable.page + 1) * pageable.size >= total,
      isFirst = pageable.page == 0,
      isEmpty = domainComments.isEmpty()
    )
  }

  override fun deleteById(commentId: String) {
    val entity = commentJpaRepository.findById(commentId.toLong())
      .orElseThrow { IllegalArgumentException("댓글을 찾을 수 없습니다.") }

    entity.status = CommentStatus.DELETED
    commentJpaRepository.save(entity)
  }

  override fun incrementLikeCount(commentId: String): Long {
    val updatedRows = commentJpaRepository.incrementLikeCount(commentId.toLong())
    if (updatedRows == 0) {
      throw IllegalArgumentException("댓글을 찾을 수 없습니다.")
    }

    return commentJpaRepository.findById(commentId.toLong())
      .map { it.likeCount }
      .orElse(0L)
  }

  override fun decrementLikeCount(commentId: String): Long {
    val updatedRows = commentJpaRepository.decrementLikeCount(commentId.toLong())
    if (updatedRows == 0) {
      throw IllegalArgumentException("댓글을 찾을 수 없거나 좋아요 수가 0입니다.")
    }

    return commentJpaRepository.findById(commentId.toLong())
      .map { it.likeCount }
      .orElse(0L)
  }

  override fun updateReplyCount(parentId: String, increment: Boolean) {
    val incrementValue = if (increment) 1L else -1L
    commentJpaRepository.updateReplyCount(parentId.toLong(), incrementValue)
  }

  override fun existsByIdAndAuthorId(commentId: String, authorId: String): Boolean {
    return commentJpaRepository.existsByIdAndAuthorId(commentId.toLong(), authorId)
  }

  private fun convertToDomain(entity: CommentEntity): Comment {
    return Comment(
      id = Tsid.encode(entity.id),
      contents = entity.contents,
      postId =  Tsid.encode(entity.postId),
      authorId = Tsid.encode(entity.authorId),
      parentId = entity.parentId,
      status = entity.status,
      likeCount = entity.likeCount,
      replyCount = entity.replyCount,
      createdAt = entity.createdAt!!,
      updatedAt = entity.updatedAt!!
    )
  }
}
package com.feedflow.application.service.comment

import com.feedflow.domain.comment.Comment
import com.feedflow.domain.comment.CommentLike
import com.feedflow.domain.model.Page
import com.feedflow.domain.model.Pageable
import com.feedflow.domain.port.comment.CommentLikeRepository
import com.feedflow.domain.port.comment.CommentRepository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

class CommentService(
  private val commentRepository: CommentRepository,
  private val commentLikeRepository: CommentLikeRepository,
) {

  @Transactional
  fun createComment(request: Comment): Comment {
    validateCommentCreation(request)
    val comment = Comment.create(
      contents = request.contents,
      postId = request.postId,
      authorId = request.authorId,
      parentId = request.parentId,
    )

    val savedComment = commentRepository.save(comment)

    request.parentId?.let { parentId ->
      commentRepository.updateReplyCount(parentId, increment = true)
    }

    return savedComment
  }

  @Transactional(readOnly = true)
  fun getComments(postId: String, pageable: Pageable): Page<Comment> {
    return commentRepository.findByPostId(postId, pageable)
  }

  @Transactional(readOnly = true)
  fun getReplies(parentId: String, pageable: Pageable): Page<Comment> {
    return commentRepository.findRepliesByParentId(parentId, pageable)
  }

  @Transactional
  fun updateComment(commentId: String, contents: String, authorId: String): Comment {
    require(commentRepository.existsByIdAndAuthorId(commentId, authorId)) {
      "댓글 수정 권한이 없습니다."
    }

    val comment = commentRepository.findById(commentId)
      ?: throw IllegalArgumentException("댓글을 찾을 수 없습니다.")

    val updatedComment = comment.copy(
      contents = contents,
      updatedAt = LocalDateTime.now()
    )

    return commentRepository.save(updatedComment)
  }

  @Transactional
  fun deleteComment(commentId: String, authorId: String) {
    require(commentRepository.existsByIdAndAuthorId(commentId, authorId)) {
      "댓글 삭제 권한이 없습니다."
    }

    val comment = commentRepository.findById(commentId)
      ?: throw IllegalArgumentException("댓글을 찾을 수 없습니다.")

    comment.parentId?.let { parentId ->
      commentRepository.updateReplyCount(parentId, increment = false)
    }

    commentRepository.deleteById(commentId)
  }

  @Transactional
  fun likeComment(commentId: String, userId: String): Long {
    if (commentLikeRepository.existsByCommentIdAndUserId(commentId, userId)) {
      throw IllegalStateException("이미 좋아요를 눌렀습니다.")
    }

    commentLikeRepository.save(CommentLike(commentId, userId))
    return commentRepository.incrementLikeCount(commentId)
  }

  @Transactional
  fun unlikeComment(commentId: String, userId: String): Long {
    if (!commentLikeRepository.existsByCommentIdAndUserId(commentId, userId)) {
      throw IllegalStateException("좋아요를 누르지 않았습니다.")
    }

    commentLikeRepository.deleteByCommentIdAndUserId(commentId, userId)
    return commentRepository.decrementLikeCount(commentId)
  }

  private fun validateCommentCreation(request: Comment) {
    require(request.contents.isNotBlank()) { "댓글 내용은 필수입니다." }
    require(request.contents.length <= 1000) { "댓글은 1000자를 초과할 수 없습니다." }

    // 부모 댓글 존재 확인
    request.parentId?.let { parentId ->
      val parentComment = commentRepository.findById(parentId)
        ?: throw IllegalArgumentException("부모 댓글을 찾을 수 없습니다.")
      require(parentComment.isActive()) { "삭제된 댓글에는 답글을 달 수 없습니다." }
    }
  }
}
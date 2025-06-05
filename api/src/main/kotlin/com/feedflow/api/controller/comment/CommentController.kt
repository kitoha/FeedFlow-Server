package com.feedflow.api.controller.comment

import com.feedflow.api.request.CreateCommentRequest
import com.feedflow.api.request.UpdateCommentRequest
import com.feedflow.api.response.CommentResponse
import com.feedflow.api.response.LikeResponse
import com.feedflow.application.service.comment.CommentService
import com.feedflow.domain.model.Page
import com.feedflow.domain.model.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/comments")
class CommentController(
  private val commentService: CommentService
) {

  @PostMapping
  fun createComment(
    @RequestBody request: CreateCommentRequest,
    @RequestHeader("X-User-Id") userId: String
  ): ResponseEntity<CommentResponse> {
    val comment = commentService.createComment(request.toComment(userId))
    return ResponseEntity.ok(CommentResponse.from(comment))
  }

  @GetMapping("/post/{postId}")
  fun getComments(
    @PathVariable postId: String,
    pageable: Pageable
  ): ResponseEntity<Page<CommentResponse>> {
    val comments = commentService.getComments(postId, pageable)
    val commentResponses = comments.content.map { CommentResponse.from(it) }
    val pageResponse = Page(
      content = commentResponses,
      pageable = comments.pageable,
      totalElements = comments.totalElements,
      totalPages = comments.totalPages,
      isFirst = comments.isFirst,
      isLast = comments.isLast,
      isEmpty = comments.isEmpty
    )
    return ResponseEntity.ok(pageResponse)
  }

  @GetMapping("/{commentId}/replies")
  fun getReplies(
    @PathVariable commentId: String,
    pageable: Pageable
  ): ResponseEntity<Page<CommentResponse>> {
    val replies = commentService.getReplies(commentId, pageable)
    val commentResponses = replies.content.map { CommentResponse.from(it) }
    val pageResponse = Page(
      content = commentResponses,
      pageable = replies.pageable,
      totalElements = replies.totalElements,
      totalPages = replies.totalPages,
      isFirst = replies.isFirst,
      isLast = replies.isLast,
      isEmpty = replies.isEmpty
    )
    return ResponseEntity.ok(pageResponse)
  }

  @PutMapping("/{commentId}")
  fun updateComment(
    @PathVariable commentId: String,
    @RequestBody request: UpdateCommentRequest,
    @RequestHeader("X-User-Id") userId: String
  ): ResponseEntity<CommentResponse> {
    val comment = commentService.updateComment(commentId, request.contents, userId)
    return ResponseEntity.ok(CommentResponse.from(comment))
  }

  @DeleteMapping("/{commentId}")
  fun deleteComment(
    @PathVariable commentId: String,
    @RequestHeader("X-User-Id") userId: String
  ): ResponseEntity<Unit> {
    commentService.deleteComment(commentId, userId)
    return ResponseEntity.noContent().build()
  }

  @PostMapping("/{commentId}/like")
  fun likeComment(
    @PathVariable commentId: String,
    @RequestHeader("X-User-Id") userId: String
  ): ResponseEntity<LikeResponse> {
    val likeCount = commentService.likeComment(commentId, userId)
    return ResponseEntity.ok(LikeResponse(likeCount))
  }

  @DeleteMapping("/{commentId}/like")
  fun unlikeComment(
    @PathVariable commentId: String,
    @RequestHeader("X-User-Id") userId: String
  ): ResponseEntity<LikeResponse> {
    val likeCount = commentService.unlikeComment(commentId, userId)
    return ResponseEntity.ok(LikeResponse(likeCount))
  }
}
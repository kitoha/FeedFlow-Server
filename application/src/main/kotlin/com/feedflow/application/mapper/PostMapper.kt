package com.feedflow.application.mapper

import com.feedflow.application.dto.post.PostResponse
import com.feedflow.application.service.storage.FileService
import com.feedflow.domain.model.post.Post
import org.springframework.stereotype.Component

@Component
class PostMapper(private val fileService: FileService) {
  companion object {
    private const val MEDIA_BUCKET = "media-bucket"
  }

  fun toPostResponse(post: Post): PostResponse = PostResponse(
    id = post.id,
    authorId = post.authorId,
    content = post.content,
    fileUrls = generateFileUrls(post.fileKeys),
    visibility = post.visibility.name,
    createdAt = post.createdAt.toString(),
    updatedAt = post.updatedAt.toString(),
    deletedAt = post.deletedAt?.toString()
  )

  private fun generateFileUrls(fileKeys: List<String>): List<String> =
    fileKeys.map { fileKey ->
      fileService.generateDownloadPresignedUrl(fileKey, MEDIA_BUCKET)
    }
}
package com.feedflow.api.request

import com.feedflow.domain.enums.post.VisibilityType
import com.feedflow.domain.model.post.Post
import com.feedflow.domain.utils.Tsid
import java.time.LocalDateTime

data class PostRequest(
  val authorId: String,
  val content: String,
  val mediaUrls: List<String>,
  val visibility: String
){
  fun toPost(): Post{
    val now = LocalDateTime.now()
    return Post(id = Tsid.generate(),
      authorId = authorId,
      content = content,
      mediaUrls =  mediaUrls.toMutableList(),
      visibility = VisibilityType.valueOf(visibility),
      createdAt = now,
      updatedAt = now,
      deletedAt = null
    )
  }
}

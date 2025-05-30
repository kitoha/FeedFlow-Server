package com.feedflow.domain.model.post

import com.feedflow.domain.enums.post.VisibilityType
import java.time.LocalDateTime

data class Post(
  val id: String,
  val authorId: String,
  var content: String,
  var fileKeys: MutableList<String> = mutableListOf(),
  var visibility: VisibilityType,
  val createdAt: LocalDateTime,
  var updatedAt: LocalDateTime,
  val deletedAt: LocalDateTime?
) {

}
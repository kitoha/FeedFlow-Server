package com.feedflow.domain.port.post

import com.feedflow.domain.model.post.Post

interface PostRepository {
  fun save(post : Post): Post
}
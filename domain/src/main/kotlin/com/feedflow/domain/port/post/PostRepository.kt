package com.feedflow.domain.port.post

import com.feedflow.domain.model.Page
import com.feedflow.domain.model.Pageable
import com.feedflow.domain.model.post.Post

interface PostRepository {
  fun save(post : Post): Post

  fun findPosts(pageable: Pageable): Page<Post>
}
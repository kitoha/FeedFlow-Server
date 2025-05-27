package com.feedflow.application.service.post

import com.feedflow.domain.model.Page
import com.feedflow.domain.model.Pageable
import com.feedflow.domain.model.post.Post
import com.feedflow.domain.port.post.PostRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostService(private val postRepository: PostRepository) {
  @Transactional
  fun createPost(post:Post){
    postRepository.save(post)
  }

  @Transactional(readOnly = true)
  fun findPosts(pageable: Pageable): Page<Post>{
    return postRepository.findPosts(pageable)
  }

}
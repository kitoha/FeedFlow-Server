package com.feedflow.application.service.post

import com.feedflow.application.dto.post.PostResponse
import com.feedflow.application.mapper.PostMapper
import com.feedflow.domain.model.Page
import com.feedflow.domain.model.Pageable
import com.feedflow.domain.model.post.Post
import com.feedflow.domain.port.post.PostRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostService(private val postRepository: PostRepository,
  private val postMapper: PostMapper) {
  @Transactional
  fun createPost(post:Post){
    postRepository.save(post)
  }

  @Transactional(readOnly = true)
  fun findPosts(pageable: Pageable): Page<PostResponse>{
    val posts = postRepository.findPosts(pageable)
    val postResponses = posts.content.map(postMapper::toPostResponse)
    return Page(content = postResponses, pageable = posts.pageable, totalElements =  posts.totalElements,
      totalPages = posts.totalPages, isFirst = posts.isFirst, isLast = posts.isLast, isEmpty = posts.isEmpty);
  }

}
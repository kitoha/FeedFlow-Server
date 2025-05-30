package com.feedflow.infrastructure.repository.post

import com.feedflow.domain.model.Page
import com.feedflow.domain.model.Pageable
import com.feedflow.domain.model.post.Post
import com.feedflow.domain.port.post.PostRepository
import com.feedflow.infrastructure.entity.post.PostEntity
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository


@Repository
class PostRepositoryImpl(private val postJpaRepository: PostJpaRepository) : PostRepository {

  override fun save(post: Post): Post {
    val postEntity = PostEntity.from(post)

    return postJpaRepository.save(postEntity).toPost()
  }

  override fun findPosts(pageable: Pageable): Page<Post> {
    val pageRequest = PageRequest.of(pageable.page, pageable.size)

    val postEntityPage = postJpaRepository.findAll(pageRequest)

    val postList = postEntityPage.content.map { it.toPost() }

    return Page(
      content = postList,
      pageable = pageable,
      totalElements = postEntityPage.totalElements,
      totalPages = postEntityPage.totalPages,
      isLast = postEntityPage.isLast,
      isFirst = postEntityPage.isFirst,
      isEmpty = postEntityPage.isEmpty
    )
  }
}
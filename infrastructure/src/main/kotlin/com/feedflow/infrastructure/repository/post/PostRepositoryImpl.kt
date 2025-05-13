package com.feedflow.infrastructure.repository.post

import com.feedflow.domain.model.post.Post
import com.feedflow.domain.port.post.PostRepository
import com.feedflow.infrastructure.entity.post.PostEntity
import org.springframework.stereotype.Repository

@Repository
class PostRepositoryImpl(private val postJpaRepository: PostJpaRepository) : PostRepository {

  override fun save(post: Post): Post {
    val postEntity = PostEntity.from(post)

    return postJpaRepository.save(postEntity).toPost()
  }
}
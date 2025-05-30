package com.feedflow.infrastructure.repository.post

import com.feedflow.infrastructure.entity.post.PostEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PostJpaRepository : JpaRepository<PostEntity, Long> {
}
package com.feedflow.infrastructure.repository

import com.feedflow.infrastructure.entity.user.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserJpaRepository : JpaRepository<UserEntity, Long>{
  fun findByEmail(email: String) : UserEntity?
}
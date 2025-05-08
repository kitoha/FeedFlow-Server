package com.feedflow.infrastructure.repository

import com.feedflow.infrastructure.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserJpaRepository : JpaRepository<UserEntity, Long>{
}
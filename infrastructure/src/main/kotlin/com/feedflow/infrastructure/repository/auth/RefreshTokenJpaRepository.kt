package com.feedflow.infrastructure.repository.auth

import com.feedflow.infrastructure.entity.auth.RefreshTokenEntity
import org.springframework.data.jpa.repository.JpaRepository

interface RefreshTokenJpaRepository : JpaRepository<RefreshTokenEntity, Long> {
  fun findByToken(token: String): RefreshTokenEntity?
  fun deleteByToken(token: String)
}
package com.feedflow.infrastructure.repository.auth

import com.feedflow.domain.model.auth.RefreshToken
import com.feedflow.domain.model.post.RefreshTokenPort
import com.feedflow.infrastructure.entity.auth.RefreshTokenEntity
import org.springframework.stereotype.Component

@Component
class RefreshTokenAdapter(
  private val refreshTokenRepository: RefreshTokenJpaRepository
) : RefreshTokenPort {
  override fun save(refreshToken: RefreshToken) {
    val entity = RefreshTokenEntity(
      token = refreshToken.token,
      userId = refreshToken.userId,
      expireAt = refreshToken.expireAt
    )
    refreshTokenRepository.save(entity)
  }

  override fun findByToken(token: String): RefreshToken? {
    return refreshTokenRepository.findByToken(token)?.let {
      RefreshToken(it.token, it.userId, it.expireAt)
    }
  }

  override fun deleteByToken(token: String) {
    refreshTokenRepository.deleteByToken(token)
  }
}
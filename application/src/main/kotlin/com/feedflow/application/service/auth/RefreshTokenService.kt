package com.feedflow.application.service.auth

import com.feedflow.domain.exception.RefreshTokenExpiredException
import com.feedflow.domain.exception.RefreshTokenNotFoundException
import com.feedflow.domain.model.auth.RefreshToken
import com.feedflow.domain.model.post.RefreshTokenPort
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration

@Service
class RefreshTokenService(
  private val refreshTokenPort: RefreshTokenPort,
  @Value("\${auth.refresh-token.expire-days:14}")
  private val refreshTokenExpireDays: Long
) {
  private val refreshTokenDuration: Duration = Duration.ofDays(refreshTokenExpireDays)

  @Transactional
  fun createRefreshToken(userId: Long): RefreshToken {
    val refreshToken = RefreshToken.create(userId, refreshTokenDuration)
    refreshTokenPort.save(refreshToken)
    return refreshToken
  }

  @Transactional
  fun rotateRefreshToken(oldToken: String, userId: Long): RefreshToken {
    refreshTokenPort.deleteByToken(oldToken)
    return createRefreshToken(userId)
  }

  @Transactional
  fun verifyAndGetUserId(token: String): Long {
    val refreshToken = refreshTokenPort.findByToken(token)
      ?: throw RefreshTokenNotFoundException()

    if (refreshToken.isExpired()) {
      refreshTokenPort.deleteByToken(token)
      throw RefreshTokenExpiredException()
    }

    return refreshToken.userId
  }

  @Transactional
  fun invalidateRefreshToken(token: String) {
    refreshTokenPort.deleteByToken(token)
  }
}
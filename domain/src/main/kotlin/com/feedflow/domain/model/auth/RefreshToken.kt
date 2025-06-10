package com.feedflow.domain.model.auth

import java.time.Duration
import java.time.Instant
import java.util.UUID

data class RefreshToken(
  val token: String,
  val userId: Long,
  val expireAt: Instant
) {
  fun isExpired(): Boolean = expireAt.isBefore(Instant.now())

  companion object {
    fun create(userId: Long, expirationPeriod: Duration): RefreshToken {
      return RefreshToken(
        token = UUID.randomUUID().toString(),
        userId = userId,
        expireAt = Instant.now().plus(expirationPeriod)
      )
    }
  }
}

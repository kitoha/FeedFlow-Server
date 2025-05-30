package com.feedflow.domain.model.post

import com.feedflow.domain.model.auth.RefreshToken

interface RefreshTokenPort {
  fun save(refreshToken: RefreshToken)
  fun findByToken(token: String): RefreshToken?
  fun deleteByToken(token: String)
}
package com.feedflow.domain.exception

sealed class RefreshTokenException(message: String) : RuntimeException(message)
class RefreshTokenNotFoundException : RefreshTokenException("RefreshToken을 찾을 수 없습니다.")
class RefreshTokenExpiredException : RefreshTokenException("RefreshToken이 만료되었습니다.")
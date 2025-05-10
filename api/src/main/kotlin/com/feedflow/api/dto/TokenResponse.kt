package com.feedflow.api.dto

data class TokenResponse (
  val token: String,
  val tokenType: String = DEFAULT_TOKEN_TYPE
) {
  companion object {
    const val DEFAULT_TOKEN_TYPE = "Bearer"
  }
}
package com.feedflow.domain.enums.auth

import java.util.Locale

enum class AuthProviderType(val key: String)  {
  GOOGLE("google");

  companion object {
    private val lookup: Map<String, AuthProviderType> =
      entries.associateBy { it.key.lowercase(Locale.ROOT) }

    fun from(provider: String): AuthProviderType =
      lookup[provider.lowercase(Locale.ROOT)]
        ?: throw IllegalArgumentException("지원하지 않는 AuthProviderType: $provider")
  }
}
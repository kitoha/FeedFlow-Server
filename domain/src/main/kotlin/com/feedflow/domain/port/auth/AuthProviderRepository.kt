package com.feedflow.domain.port.auth

import com.feedflow.domain.model.auth.AuthProvider

interface AuthProviderRepository {
  fun save(authProvider: AuthProvider): AuthProvider
}
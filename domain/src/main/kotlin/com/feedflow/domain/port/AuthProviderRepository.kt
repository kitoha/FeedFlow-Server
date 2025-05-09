package com.feedflow.domain.port

import com.feedflow.domain.model.AuthProvider

interface AuthProviderRepository {
  fun save(authProvider: AuthProvider): AuthProvider
}
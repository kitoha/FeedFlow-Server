package com.feedflow.infrastructure.repository.auth

import com.feedflow.domain.model.auth.AuthProvider
import com.feedflow.domain.port.auth.AuthProviderRepository
import com.feedflow.infrastructure.entity.auth.AuthProviderEntity
import org.springframework.stereotype.Repository

@Repository
class AuthProviderRepositoryImpl(
  private val authProviderJpaRepository: AuthProviderJpaRepository
) : AuthProviderRepository {

  override fun save(authProvider: AuthProvider): AuthProvider {
    val authProviderEntity = AuthProviderEntity.from(authProvider)
    return authProviderJpaRepository.save(authProviderEntity).toAuthProvider()
  }
}
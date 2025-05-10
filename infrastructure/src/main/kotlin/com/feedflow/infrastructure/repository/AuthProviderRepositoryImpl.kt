package com.feedflow.infrastructure.repository

import com.feedflow.domain.model.AuthProvider
import com.feedflow.domain.port.AuthProviderRepository
import com.feedflow.infrastructure.entity.AuthProviderEntity
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
package com.feedflow.domain.model

import com.feedflow.domain.enums.auth.AuthProviderType
import com.feedflow.domain.utils.Tsid
import java.time.LocalDateTime

data class AuthProvider(
  val id: String,
  val authProviderType: AuthProviderType,
  val providerId: String,
  val email: String,
  val createdAt: LocalDateTime,
  val updatedAt: LocalDateTime,
  val user: User
) {

  companion object{
    fun createNew(providerId : String, email : String, authProviderType: AuthProviderType,user: User) : AuthProvider{
      val now = LocalDateTime.now()
      return AuthProvider(
        id = Tsid.generate(),
        authProviderType = authProviderType,
        providerId = providerId,
        email = email,
        createdAt = now,
        updatedAt = now,
        user = user
      )
    }
  }
}
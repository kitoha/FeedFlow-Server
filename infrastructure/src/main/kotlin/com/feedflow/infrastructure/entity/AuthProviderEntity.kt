package com.feedflow.infrastructure.entity

import com.feedflow.domain.enums.AuthProviderType
import com.feedflow.domain.model.AuthProvider
import com.feedflow.domain.utils.Tsid
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "auth_provider_entity")
class AuthProviderEntity(
  @Id
  val id: Long,

  @Column(name = "auth_provider_type")
  @Enumerated(EnumType.STRING)
  val authProviderType: AuthProviderType,

  @Column(name = "provider_id")
  val providerId: String,

  @Column(name = "email")
  val email: String,

  @Column(name = "created_at")
  val createdAt: LocalDateTime,

  @Column(name = "updated_at")
  val updatedAt: LocalDateTime,

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  val user: UserEntity
) {

  fun toAuthProvider(): AuthProvider{
    return AuthProvider(
      id = Tsid.encode(id),
      authProviderType = authProviderType,
      providerId = providerId,
      email = email,
      createdAt = createdAt,
      updatedAt = updatedAt,
      user = user.toUser()
    )
  }

  companion object{
    fun from(authProvider: AuthProvider): AuthProviderEntity{
      return AuthProviderEntity(
        id = Tsid.decode(authProvider.id),
        authProviderType = authProvider.authProviderType,
        providerId = authProvider.providerId,
        email = authProvider.email,
        createdAt = authProvider.createdAt,
        updatedAt = authProvider.updatedAt,
        user = UserEntity.from(authProvider.user)
      )
    }
  }
}
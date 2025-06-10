package com.feedflow.infrastructure.entity.auth

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "refresh_tokens")
class RefreshTokenEntity(
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,

  @Column(nullable = false, unique = true)
  val token: String,

  @Column(nullable = false)
  val userId: Long,

  @Column(nullable = false)
  val expireAt: Instant
)

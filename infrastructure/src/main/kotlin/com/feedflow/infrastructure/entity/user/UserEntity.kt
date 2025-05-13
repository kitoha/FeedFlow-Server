package com.feedflow.infrastructure.entity.user

import com.feedflow.domain.enums.user.UserStatus
import com.feedflow.domain.model.user.User
import com.feedflow.domain.utils.Tsid
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class UserEntity(
  @Id
  val id: Long,

  @Column(name = "email")
  val email: String,

  @Column(name = "user_status")
  @Enumerated(EnumType.STRING)
  val userStatus: UserStatus,

  @Column(name = "last_login_at")
  val lastLoginAt: LocalDateTime,

  @Column(name = "created_at")
  val createdAt: LocalDateTime,

  @Column(name = "updated_at")
  val updatedAt: LocalDateTime,

  @Column
  val dormantAt: LocalDateTime?,

  @Column
  val deletedAt: LocalDateTime?
) {
  fun toUser(): User {
    return User(
      id = Tsid.encode(id),
      email = email,
      userStatus = userStatus,
      lastLoginAt = lastLoginAt,
      createdAt = createdAt,
      updatedAt = updatedAt,
      dormantAt = dormantAt,
      deletedAt = deletedAt
    )
  }

  companion object{
    fun from(user: User): UserEntity {
      return UserEntity(
        id = Tsid.decode(user.id),
        email = user.email,
        userStatus = user.userStatus,
        lastLoginAt = user.lastLoginAt,
        createdAt = user.createdAt,
        updatedAt = user.updatedAt,
        dormantAt = user.dormantAt,
        deletedAt = user.deletedAt
      )
    }
  }


}
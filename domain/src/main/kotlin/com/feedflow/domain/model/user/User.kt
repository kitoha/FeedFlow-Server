package com.feedflow.domain.model.user

import com.feedflow.domain.enums.user.UserStatus
import com.feedflow.domain.utils.Tsid
import java.time.LocalDateTime

data class User(
  val id : String,
  val email: String,
  val userStatus: UserStatus,
  val lastLoginAt: LocalDateTime,
  val createdAt: LocalDateTime,
  val updatedAt: LocalDateTime,
  val dormantAt: LocalDateTime?,
  val deletedAt: LocalDateTime?
){
  companion object {
    fun createNew(email: String): User {
      val now = LocalDateTime.now()
      return User(
        id = Tsid.generate(),
        email       = email,
        userStatus  = UserStatus.ACTIVE,
        lastLoginAt = now,
        createdAt   = now,
        updatedAt   = now,
        dormantAt   = null,
        deletedAt   = null
      )
    }
  }
}
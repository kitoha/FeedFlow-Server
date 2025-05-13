package com.feedflow.domain.port.user

import com.feedflow.domain.model.user.User

interface UserRepository {
  fun save(user: User): User

  fun findUser(email: String): User?
}
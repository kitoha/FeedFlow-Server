package com.feedflow.domain.port

import com.feedflow.domain.model.User

interface UserRepository {
  fun save(user: User): User

  fun findUser(email: String): User?
}
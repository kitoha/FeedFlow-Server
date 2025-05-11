package com.feedflow.infrastructure.repository

import com.feedflow.domain.model.User
import com.feedflow.domain.port.UserRepository
import com.feedflow.infrastructure.entity.user.UserEntity
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl (private val userJpaRepository: UserJpaRepository) : UserRepository {
  override fun save(user: User): User {
    val userEntity = UserEntity.from(user)
    val savedUser = userJpaRepository.save(userEntity)
    return savedUser.toUser()
  }

  override fun findUser(email: String): User? {
    return userJpaRepository.findByEmail(email)
      ?.toUser()
  }
}
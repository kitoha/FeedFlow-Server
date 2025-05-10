package com.feedflow.application.service

import com.feedflow.application.dto.OAuth2UserInfo
import com.feedflow.domain.enums.AuthProviderType
import com.feedflow.domain.model.AuthProvider
import com.feedflow.domain.model.User
import com.feedflow.domain.port.AuthProviderRepository
import com.feedflow.domain.port.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
  private val userRepository: UserRepository,
  private val authProviderRepository: AuthProviderRepository
) {

  fun getUserInfo(oAuth2UserInfo: OAuth2UserInfo): User{
    val existing = userRepository.findUser(oAuth2UserInfo.email)
    return existing ?: join(oAuth2UserInfo)
  }
  fun join(oAuth2UserInfo: OAuth2UserInfo): User{
    val user = User.createNew(oAuth2UserInfo.email)
    val authProviderType = AuthProviderType.from(oAuth2UserInfo.provider)
    val authProvider = AuthProvider.createNew(oAuth2UserInfo.providerId, oAuth2UserInfo.email, authProviderType, user)
    val newUser = userRepository.save(user)
    authProviderRepository.save(authProvider)
    return newUser
  }
}
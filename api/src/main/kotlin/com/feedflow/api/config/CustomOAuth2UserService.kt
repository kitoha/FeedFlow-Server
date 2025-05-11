package com.feedflow.api.config

import com.feedflow.api.dto.CustomOAuth2User
import com.feedflow.application.port.OAuth2UserInfoExtractor
import com.feedflow.application.service.user.UserService
import com.feedflow.domain.enums.auth.AuthProviderType
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class CustomOAuth2UserService (private val userService: UserService,
  private val extractors: List<OAuth2UserInfoExtractor>): OAuth2UserService<OAuth2UserRequest, OAuth2User>{

  override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {

    val delegate = DefaultOAuth2UserService()
    val oauthUser = delegate.loadUser(userRequest)

    val attributes = oauthUser.attributes
    val authorities = oauthUser.authorities
    val clientRegistration = userRequest.clientRegistration
    val registrationType = AuthProviderType.from(clientRegistration.registrationId)

    val extractor = extractors.find { it.supports(registrationType) }
      ?: throw IllegalArgumentException("지원하지 않는 OAuth2 제공자입니다: $registrationType")

    val oAuth2UserInfo = extractor.extractUserInfo(attributes)

    val userNameAttr = userRequest
      .clientRegistration
      .providerDetails
      .userInfoEndpoint
      .userNameAttributeName

    val user = userService.getUserInfo(oAuth2UserInfo)

    return CustomOAuth2User(DefaultOAuth2User(authorities, attributes, userNameAttr), user.id)
  }
}
package com.feedflow.api.config

import com.feedflow.domain.model.User
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class CustomOAuth2UserService (
  private val delegate: OAuth2UserService<OAuth2UserRequest, OAuth2User>,
) : OAuth2UserService<OAuth2UserRequest, OAuth2User>{

  override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
    val oauthUser = delegate.loadUser(userRequest)

    val attributes = oauthUser.attributes
    val authorities = oauthUser.authorities
    val userName = attributes["name"].toString()
    val userEmail = attributes["email"].toString()

    val userNameAttr = userRequest
      .clientRegistration
      .providerDetails
      .userInfoEndpoint
      .userNameAttributeName

    val user = User(userName, userEmail)

    return DefaultOAuth2User(authorities, attributes, userNameAttr)
  }
}
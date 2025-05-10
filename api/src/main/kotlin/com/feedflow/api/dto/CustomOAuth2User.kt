package com.feedflow.api.dto

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User

class CustomOAuth2User(
  private val oauth2User: OAuth2User,
  private val userId: String,
) : OAuth2User{

  override fun getAttributes(): Map<String, Any> {
    return oauth2User.attributes
  }

  override fun getAuthorities(): Collection<GrantedAuthority> {
    return oauth2User.authorities
  }

  override fun getName(): String {
    return oauth2User.name
  }

  fun getUserId(): String {
    return userId
  }
}
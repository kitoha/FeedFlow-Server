package com.feedflow.infrastructure.extractor

import com.feedflow.application.dto.OAuth2UserInfo
import com.feedflow.application.port.OAuth2UserInfoExtractor
import com.feedflow.domain.enums.auth.AuthProviderType
import org.springframework.stereotype.Component

@Component
class GoogleOAuth2UserInfoExtractor : OAuth2UserInfoExtractor{

  override fun extractUserInfo(attributes: Map<String, Any>): OAuth2UserInfo {
    return OAuth2UserInfo(
      providerId = attributes["sub"] as String,
      email = attributes["email"] as String,
      provider = "google"
    )
  }

  override fun supports(authProviderType: AuthProviderType): Boolean {
    return authProviderType == AuthProviderType.GOOGLE
  }
}
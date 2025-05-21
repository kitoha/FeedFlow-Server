package com.feedflow.application.port.oauth

import com.feedflow.application.dto.oauth.OAuth2UserInfo
import com.feedflow.domain.enums.auth.AuthProviderType

interface OAuth2UserInfoExtractor {
  fun extractUserInfo(attributes: Map<String, Any>): OAuth2UserInfo
  fun supports(authProviderType: AuthProviderType): Boolean
}
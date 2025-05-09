package com.feedflow.application.port

import com.feedflow.application.dto.OAuth2UserInfo
import com.feedflow.domain.enums.AuthProviderType

interface OAuth2UserInfoExtractor {
  fun extractUserInfo(attributes: Map<String, Any>): OAuth2UserInfo
  fun supports(authProviderType: AuthProviderType): Boolean
}
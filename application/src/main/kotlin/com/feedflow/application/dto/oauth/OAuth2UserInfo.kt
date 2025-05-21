package com.feedflow.application.dto.oauth

data class OAuth2UserInfo(
  val providerId: String,
  val email : String,
  val provider : String
) {
}
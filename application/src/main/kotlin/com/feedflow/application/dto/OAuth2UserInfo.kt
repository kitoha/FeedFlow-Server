package com.feedflow.application.dto

data class OAuth2UserInfo(
  val providerId: String,
  val email : String,
  val provider : String
) {
}
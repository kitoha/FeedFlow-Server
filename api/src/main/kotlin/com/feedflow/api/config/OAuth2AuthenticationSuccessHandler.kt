package com.feedflow.api.config

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets

@Component
class OAuth2AuthenticationSuccessHandler(
  private val jwtTokenProvider: JwtTokenProvider,
  private val objectMapper: ObjectMapper
) : AuthenticationSuccessHandler {

  override fun onAuthenticationSuccess(
    request: HttpServletRequest,
    response: HttpServletResponse,
    authentication: Authentication
  ) {
    try {
      val oAuth2User = authentication.principal as OAuth2User

    } catch (e: Exception) {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Token generation failed")
    }
  }
}
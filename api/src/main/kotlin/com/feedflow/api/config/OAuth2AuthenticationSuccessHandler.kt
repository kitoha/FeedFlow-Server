package com.feedflow.api.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.feedflow.api.dto.CustomOAuth2User
import com.feedflow.api.dto.TokenResponse
import com.feedflow.domain.utils.Tsid
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
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
      val oAuth2User = authentication.principal as CustomOAuth2User
      val userId = oAuth2User.getUserId()

      val token = jwtTokenProvider.generateToken(Tsid.decode(userId))
      val tokenResponse = TokenResponse(token = token)

      response.addHeader(HttpHeaders.AUTHORIZATION, "${TokenResponse.DEFAULT_TOKEN_TYPE} $token")

      response.contentType = MediaType.APPLICATION_JSON_VALUE
      response.characterEncoding = StandardCharsets.UTF_8.name()

      objectMapper.writeValue(response.writer, tokenResponse)
      response.writer.flush()
    } catch (e: Exception) {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Token generation failed")
    }
  }
}
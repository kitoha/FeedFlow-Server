package com.feedflow.api.config

import com.feedflow.api.dto.CustomOAuth2User
import com.feedflow.api.dto.TokenResponse
import com.feedflow.domain.utils.Tsid
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

@Component
class OAuth2AuthenticationSuccessHandler(
  private val jwtTokenProvider: JwtTokenProvider,
  private val redirectProps: RedirectProperties
) : AuthenticationSuccessHandler {

  companion object {
    private const val PARAM_TOKEN       = "token"
    private const val PARAM_TOKEN_TYPE  = "tokenType"
    private const val PARAM_ERROR       = "error"
    private const val ERR_TOKEN_FAILED  = "token_generation_failed"
  }

  override fun onAuthenticationSuccess(
    request: HttpServletRequest,
    response: HttpServletResponse,
    authentication: Authentication
  ) {
    try {
      val oAuth2User = authentication.principal as CustomOAuth2User
      val userId = oAuth2User.getUserId()

      val token = jwtTokenProvider.generateToken(Tsid.decode(userId))

      val redirectUri = UriComponentsBuilder
        .fromUriString(redirectProps.success)
        .queryParam(PARAM_TOKEN, token)
        .queryParam(PARAM_TOKEN_TYPE, TokenResponse.DEFAULT_TOKEN_TYPE)
        .build()
        .toUriString()

      response.sendRedirect(redirectUri)
    } catch (e: Exception) {
      val redirectUrl = UriComponentsBuilder
        .fromUriString(redirectProps.failure)
        .queryParam(PARAM_ERROR, ERR_TOKEN_FAILED)
        .build()
        .toUriString()

      response.sendRedirect(redirectUrl)
    }
  }
}
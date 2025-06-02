package com.feedflow.api.config

import com.feedflow.api.dto.CustomOAuth2User
import com.feedflow.application.service.auth.RefreshTokenService
import com.feedflow.domain.utils.Tsid
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

@Component
class OAuth2AuthenticationSuccessHandler(
  private val refreshTokenService: RefreshTokenService,
  private val jwtTokenProvider: JwtTokenProvider,
  private val redirectProps: RedirectProperties
) : AuthenticationSuccessHandler {
  companion object {
    private const val REFRESH_TOKEN_COOKIE_NAME = "refresh_token"
    private const val REFRESH_TOKEN_PATH = "/"
    private const val REFRESH_TOKEN_MAX_AGE = 60 * 60 * 24 * 14 // 14 days
  }

  override fun onAuthenticationSuccess(
    request: HttpServletRequest,
    response: HttpServletResponse,
    authentication: Authentication
  ) {
    val oAuth2User = authentication.principal as CustomOAuth2User
    val userId = Tsid.decode(oAuth2User.getUserId())

    val refreshToken = refreshTokenService.createRefreshToken(userId)

    setRefreshTokenCookie(response, refreshToken.token)
    response.sendRedirect(redirectProps.success)
  }

  private fun setRefreshTokenCookie(response: HttpServletResponse, token: String) {
    val cookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, token)
      .httpOnly(true)
      .secure(true)
      .path(REFRESH_TOKEN_PATH)
      .maxAge(REFRESH_TOKEN_MAX_AGE.toLong())
      .sameSite("Strict")
      .build()

    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString())
  }
}

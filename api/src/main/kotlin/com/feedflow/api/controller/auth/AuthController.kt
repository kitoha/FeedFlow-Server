package com.feedflow.api.controller.auth

import com.feedflow.api.config.JwtTokenProvider
import com.feedflow.api.response.AccessTokenResponse
import com.feedflow.application.service.auth.RefreshTokenService
import jakarta.servlet.http.Cookie
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class AuthController(
  private val jwtTokenProvider: JwtTokenProvider,
  private val refreshTokenService: RefreshTokenService
) {

  companion object{
    const val REFRESH_TOKEN_COOKIE_NAME = "refresh_token"
    const val REFRESH_TOKEN_COOKIE_PATH = "/"
    const val REFRESH_TOKEN_COOKIE_MAX_AGE = 60 * 60 * 24 * 14 // 14 days
    const val REFRESH_TOKEN_COOKIE_HTTP_ONLY = true
    const val REFRESH_TOKEN_COOKIE_SECURE = true
    const val REFRESH_TOKEN_COOKIE_SAME_SITE = "Strict"
  }

  @PostMapping("/auth/token")
  fun issueAccessToken(@CookieValue("refresh_token") refreshToken: String): ResponseEntity<AccessTokenResponse> {
    val userId = refreshTokenService.verifyAndGetUserId(refreshToken)
    val accessToken = jwtTokenProvider.generateToken(userId)
    return ResponseEntity.ok(AccessTokenResponse(accessToken))
  }

  @PostMapping("/auth/refresh")
  fun refreshToken(@CookieValue("refresh_token") oldRefreshToken: String): ResponseEntity<AccessTokenResponse> {
    val userId = refreshTokenService.verifyAndGetUserId(oldRefreshToken)
    val newAccessToken = jwtTokenProvider.generateToken(userId)
    val newRefreshToken = refreshTokenService.rotateRefreshToken(oldRefreshToken, userId)

    val cookie = Cookie(REFRESH_TOKEN_COOKIE_NAME, newRefreshToken.token).apply {
      isHttpOnly = REFRESH_TOKEN_COOKIE_HTTP_ONLY
      secure = REFRESH_TOKEN_COOKIE_SECURE
      path = REFRESH_TOKEN_COOKIE_PATH
      maxAge = REFRESH_TOKEN_COOKIE_MAX_AGE
      setAttribute("SameSite", REFRESH_TOKEN_COOKIE_SAME_SITE)
    }
    val response = ResponseEntity.ok()
      .header(HttpHeaders.SET_COOKIE, cookie.toString())
      .body(AccessTokenResponse(newAccessToken))
    return response
  }

  @PostMapping("/auth/logout")
  fun logout(@CookieValue("refresh_token") refreshToken: String): ResponseEntity<Void> {
    refreshTokenService.invalidateRefreshToken(refreshToken)
    val cookie = Cookie(REFRESH_TOKEN_COOKIE_NAME, "").apply {
      path = REFRESH_TOKEN_COOKIE_PATH
      maxAge = 0
      isHttpOnly = REFRESH_TOKEN_COOKIE_HTTP_ONLY
      secure = REFRESH_TOKEN_COOKIE_SECURE
      setAttribute("SameSite", REFRESH_TOKEN_COOKIE_SAME_SITE)
    }
    return ResponseEntity.noContent()
      .header(HttpHeaders.SET_COOKIE, cookie.toString())
      .build()
  }

}

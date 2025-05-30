package com.feedflow.api.controller.auth

import com.feedflow.api.config.JwtTokenProvider
import com.feedflow.api.response.AccessTokenResponse
import com.feedflow.application.service.auth.RefreshTokenService
import jakarta.servlet.http.Cookie
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
  private val jwtTokenProvider: JwtTokenProvider,
  private val refreshTokenService: RefreshTokenService
) {

  @PostMapping("/token")
  fun issueAccessToken(@CookieValue("refresh_token") refreshToken: String): ResponseEntity<AccessTokenResponse> {
    val userId = refreshTokenService.verifyAndGetUserId(refreshToken)
    val accessToken = jwtTokenProvider.generateToken(userId)
    return ResponseEntity.ok(AccessTokenResponse(accessToken))
  }

  @PostMapping("/refresh")
  fun refreshToken(@CookieValue("refresh_token") oldRefreshToken: String): ResponseEntity<AccessTokenResponse> {
    val userId = refreshTokenService.verifyAndGetUserId(oldRefreshToken)
    val newAccessToken = jwtTokenProvider.generateToken(userId)
    val newRefreshToken = refreshTokenService.rotateRefreshToken(oldRefreshToken, userId)

    val cookie = Cookie("refresh_token", newRefreshToken.token).apply {
      isHttpOnly = true
      secure = true
      path = "/"
      maxAge = 60 * 60 * 24 * 14
      setAttribute("SameSite", "Strict")
    }
    val response = ResponseEntity.ok()
      .header(HttpHeaders.SET_COOKIE, cookie.toString())
      .body(AccessTokenResponse(newAccessToken))
    return response
  }

  @PostMapping("/logout")
  fun logout(@CookieValue("refresh_token") refreshToken: String): ResponseEntity<Void> {
    refreshTokenService.invalidateRefreshToken(refreshToken)
    val cookie = Cookie("refresh_token", "").apply {
      path = "/"
      maxAge = 0
      isHttpOnly = true
      secure = true
      setAttribute("SameSite", "Strict")
    }
    return ResponseEntity.noContent()
      .header(HttpHeaders.SET_COOKIE, cookie.toString())
      .build()
  }

}

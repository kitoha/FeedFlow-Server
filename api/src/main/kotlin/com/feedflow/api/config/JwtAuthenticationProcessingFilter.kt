package com.feedflow.api.config

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpHeaders
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter

class JwtAuthenticationProcessingFilter : AbstractPreAuthenticatedProcessingFilter(){

  override fun getPreAuthenticatedPrincipal(request: HttpServletRequest): Any? {
    return try {
      resolveAccessToken(request)
    } catch (e: Exception) {
      null
    }
  }

  override fun getPreAuthenticatedCredentials(request: HttpServletRequest?): Any? {
    return null
  }

  private fun resolveAccessToken(request: HttpServletRequest): String? {
    val authorization = request.getHeader(HttpHeaders.AUTHORIZATION)
    val prefix = "Bearer "
    return authorization
      ?.takeIf { it.startsWith(prefix, ignoreCase = true) }
      ?.substring(prefix.length)
      ?.trim()
      ?.takeIf { it.isNotEmpty() }
  }
}
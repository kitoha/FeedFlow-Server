package com.feedflow.api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.servlet.HandlerExceptionResolver

@Configuration
@EnableWebSecurity
class SecurityConfig(
  private val handlerExceptionResolver: HandlerExceptionResolver,
  private val customOAuth2UserService: CustomOAuth2UserService,
  private val jwtTokenProvider: JwtTokenProvider,
  private val corsConfigSource: CorsConfigurationSource,
  private val oAuth2AuthenticationSuccessHandler: OAuth2AuthenticationSuccessHandler,
  private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint
){
  @Bean
  fun filterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
    httpSecurity.authorizeHttpRequests{
      auth -> auth.requestMatchers(
        HttpMethod.GET,
        "/hello"
      ).permitAll().requestMatchers(
      HttpMethod.POST,
      "/api/v1/auth/token",
      "/api/v1/auth/refresh",
      "/api/v1/auth/logout"
    ).permitAll()
      .anyRequest().authenticated()
    }.csrf { it.disable() }
      .oauth2Login { oauth2 ->
        oauth2.userInfoEndpoint { it.userService(customOAuth2UserService) }
          .successHandler(oAuth2AuthenticationSuccessHandler)
      }
      .logout{it.disable()}
      .cors{it.configurationSource(corsConfigSource)}
      .addFilterAt(jwtAuthenticationFilter(), AbstractPreAuthenticatedProcessingFilter::class.java)
      .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
      .exceptionHandling { exceptionHandling ->
        exceptionHandling
          .authenticationEntryPoint(jwtAuthenticationEntryPoint)
          .accessDeniedHandler { request, response, accessDeniedException ->
            handlerExceptionResolver.resolveException(request, response, null, accessDeniedException)
          }
      }

    return httpSecurity.build()
  }

  @Bean
  fun jwtAuthenticationFilter() : AbstractPreAuthenticatedProcessingFilter{
    return JwtAuthenticationProcessingFilter().apply {
      setAuthenticationManager(jwtTokenAuthenticationManager())
    }
  }

  @Bean
  fun jwtTokenAuthenticationManager(): AuthenticationManager {
    return AuthenticationManager { authentication ->
      val principal = authentication.principal
      if (principal is String) {
        try {
          val memberId = jwtTokenProvider.getMemberId(principal)
          PreAuthenticatedAuthenticationToken(memberId, principal).apply {
            isAuthenticated = true
          }
        } catch (ex: Exception) {
          throw BadCredentialsException("Invalid accessToken", ex)
        }
      } else {
        throw BadCredentialsException("Invalid accessToken")
      }
    }
  }

}
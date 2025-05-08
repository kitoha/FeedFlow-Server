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

@EnableWebSecurity
@Configuration
class SecurityConfig(
  private val handlerExceptionResolver: HandlerExceptionResolver,
  private val jwtTokenProvider: JwtTokenProvider,
  private val corsConfigSource: CorsConfigurationSource
){
  @Bean
  fun filterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
    httpSecurity.authorizeHttpRequests{
      auth -> auth.requestMatchers(
        HttpMethod.GET,
        "/hello"
      ).permitAll()
      .requestMatchers(HttpMethod.POST, "/api/v1/members/login").permitAll()
      .anyRequest().authenticated()
    }
      .logout{it.disable()}
      .cors{it.configurationSource(corsConfigSource)}
      .addFilterAt(jwtAuthenticationFilter(), AbstractPreAuthenticatedProcessingFilter::class.java)
      .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
      .exceptionHandling { exceptionHandling ->
        exceptionHandling
          .authenticationEntryPoint { request, response, authException ->
            handlerExceptionResolver.resolveException(request, response, null, authException)
          }
          .accessDeniedHandler { request, response, accessDeniedException ->
            handlerExceptionResolver.resolveException(request, response, null, accessDeniedException)
          }
      }
      .build()

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
        val memberId = jwtTokenProvider.getMemberId(principal)

        PreAuthenticatedAuthenticationToken(memberId, principal).apply {
          isAuthenticated = true
        }
      } else {
        throw BadCredentialsException("Invalid accessToken")
      }
    }
  }

}
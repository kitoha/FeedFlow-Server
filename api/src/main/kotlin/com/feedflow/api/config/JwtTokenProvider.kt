package com.feedflow.api.config

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Instant
import javax.crypto.SecretKey
import java.time.Duration
import java.util.*

@Component
class JwtTokenProvider(
  private val secretKey: SecretKey,
  @Value("\${feedflow.jwt.expiration-minutes:15}") private val expirationMinutes: Long,
  @Value("\${feedflow.jwt.issuer:feedflow-api}") private val issuer: String
) {

  private val expiration: Duration =  Duration.ofMinutes(expirationMinutes)

  fun generateToken(memberId: Long): String {
    val now = Instant.now()
    val exp = now.plus(expiration)

    return Jwts.builder()
      .setSubject(memberId.toString())
      .setIssuer(issuer)
      .setIssuedAt(Date.from(now))
      .setExpiration(Date.from(exp))
      .setId(UUID.randomUUID().toString())
      .signWith(secretKey, SignatureAlgorithm.HS256)
      .compact()
  }

  fun getMemberId(token: String): Long {
    return getClaims(token).subject.toLong()
  }

  private fun getClaims(token: String): Claims {
    return Jwts.parserBuilder()
      .setSigningKey(secretKey)
      .build()
      .parseClaimsJws(token)
      .body
  }
}
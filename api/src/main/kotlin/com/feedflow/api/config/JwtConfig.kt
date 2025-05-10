package com.feedflow.api.config

import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.crypto.SecretKey

@Configuration
class JwtConfig(
  @Value("\${feedflow.jwt.secret}") private val secretKeyString: String
) {

  @Bean
  fun secretKey(): SecretKey {
    require(secretKeyString.isNotBlank()) {
      "JWT 비밀키가 설정되지 않았습니다."
    }
    val keyBytes = Decoders.BASE64.decode(secretKeyString)
    return Keys.hmacShaKeyFor(keyBytes)
  }
}

package com.feedflow.infrastructure.config.minio

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "minio")
class MinioProperties {
  lateinit var endpoint: String
  lateinit var accessKey: String
  lateinit var secretKey: String
  lateinit var buckets: Map<String, String>
}
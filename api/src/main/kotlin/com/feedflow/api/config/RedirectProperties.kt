package com.feedflow.api.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "app.oauth.redirect")
class RedirectProperties {
  lateinit var success: String
  lateinit var failure: String
}
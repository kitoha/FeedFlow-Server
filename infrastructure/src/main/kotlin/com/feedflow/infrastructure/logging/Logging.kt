package com.feedflow.infrastructure.logging

import mu.KLogger
import mu.KotlinLogging
import java.util.concurrent.ConcurrentHashMap

private val loggerCache = ConcurrentHashMap<String, KLogger>()

val <T : Any> T.log: KLogger
  get() = loggerCache.computeIfAbsent(this::class.java.name) {
    KotlinLogging.logger(it)
  }
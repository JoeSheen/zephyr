package com.shoejs.plugins

import com.shoejs.config.RedisConfig
import com.shoejs.infrastructure.redis.RedisConnectionManager
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStopping

fun Application.configureCache() {
    val config = RedisConfig.fromEnvironment(environment)

    RedisConnectionManager.connect(config)

    monitor.subscribe(ApplicationStopping) {
        RedisConnectionManager.close()
    }
}

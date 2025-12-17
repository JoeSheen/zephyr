package com.shoejs.plugins

import com.shoejs.infrastructure.redis.RedisConnectionManager
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStopping

fun Application.configureCache(host: String = "localhost", port: Int = 6379) {
    RedisConnectionManager.connect(host, port)

    monitor.subscribe(ApplicationStopping) {
        RedisConnectionManager.close()
    }
}

package com.shoejs.config

import io.ktor.server.application.ApplicationEnvironment

@ConsistentCopyVisibility
data class RedisConfig private constructor(
    val host: String, val port: Int
) {
    companion object {
        fun fromEnvironment(environment: ApplicationEnvironment): RedisConfig = RedisConfig(
            host = environment.config.property("redis.host").getString(),
            port = environment.config.property("redis.port").getString().toInt()
        )
    }
}

package com.shoejs.config

import io.ktor.server.application.ApplicationEnvironment

@ConsistentCopyVisibility
data class JwtConfig private constructor(
    val realm: String,
    val secret: String,
    val audience: String,
    val domain: String,
    val expirationOffset: Long,
    val notBeforeOffset: Long,
) {
    companion object {
        fun fromEnvironment(environment: ApplicationEnvironment): JwtConfig = JwtConfig(
            realm = environment.config.property("jwt.realm").getString(),
            secret = environment.config.property("jwt.secret").getString(),
            audience = environment.config.property("jwt.audience").getString(),
            domain = environment.config.property("jwt.domain").getString(),
            expirationOffset = 5_400_000L,
            notBeforeOffset = 30_000L
        )
    }
}

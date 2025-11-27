package com.shoejs.auth

import io.ktor.server.config.ApplicationConfig

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
        fun fromAppConfig(config: ApplicationConfig): JwtConfig {
            return JwtConfig(
                realm = config.property("jwt.realm").getString(),
                secret = config.property("jwt.secret").getString(),
                audience = config.property("jwt.audience").getString(),
                domain = config.property("jwt.domain").getString(),
                expirationOffset = 5_400_000,
                notBeforeOffset = 30_000
            )
        }
    }
}

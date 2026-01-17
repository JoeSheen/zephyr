package com.shoejs.config

import io.ktor.server.application.ApplicationEnvironment

@ConsistentCopyVisibility
data class DatabaseConfig private constructor(
    val url: String, val driver: String, val user: String, val password: String
) {
    companion object {
        fun fromEnvironment(environment: ApplicationEnvironment): DatabaseConfig = DatabaseConfig(
            url = environment.config.property("postgres.url").getString(),
            driver = environment.config.property("postgres.driver").getString(),
            user = environment.config.property("postgres.user").getString(),
            password = environment.config.property("postgres.password").getString()
        )
    }
}

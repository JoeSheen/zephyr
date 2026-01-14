package com.shoejs.plugins

import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.cors.routing.CORS
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun Application.configureCors() {
    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    val isDev = environment.config.propertyOrNull("ktor.deployment.environment")?.getString() == "dev"
    logger.info("CORS Development Deployment Environment: $isDev")

    install(CORS) {
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Head)
        allowMethod(HttpMethod.Options)

        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)

        allowCredentials = true

        if (isDev) {
            allowHost("localhost:4200", listOf("http", "https"))
            allowHost("localhost:4200", listOf("http", "https"))
        } else {
            allowHost("zephyr.com", listOf("https")) // ?
        }

        maxAgeInSeconds = 3600
    }
}

package com.shoejs.plugins

import io.ktor.server.application.Application
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun Application.configureCors() {
    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    val isDev = environment.config.propertyOrNull("ktor.deployment.environment")?.getString() == "dev"
    logger.info("Deployment Environment: $isDev")
}

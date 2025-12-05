package com.shoejs.plugins

import io.ktor.server.application.Application
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.routing.routing

fun Application.configureSwagger() {
    routing {
        swaggerUI(path = "/swagger-ui", swaggerFile = "openapi/documentation.yaml") {
            version = "5.17.12"
        }
    }
}

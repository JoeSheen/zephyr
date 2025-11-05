package com.shoejs.plugins

import com.shoejs.auth.JwtConfig
import com.shoejs.auth.JwtService
import com.shoejs.features.auth.authRoutes
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.configureRouting(config: JwtConfig) {
    routing {
        authRoutes(JwtService(config))
    }
}

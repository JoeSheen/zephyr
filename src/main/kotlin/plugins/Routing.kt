package com.shoejs.plugins

import com.shoejs.auth.JwtConfig
import com.shoejs.auth.JwtService
import com.shoejs.features.auth.AuthService
import com.shoejs.features.auth.LoginRequest
import com.shoejs.features.auth.authRoutes
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.requestvalidation.RequestValidation
import io.ktor.server.plugins.requestvalidation.ValidationResult
import io.ktor.server.routing.routing

fun Application.configureRouting(config: JwtConfig) {
    install(RequestValidation) {
        validate<LoginRequest> { request ->
            when {
                request.username.isBlank() -> ValidationResult.Invalid("Username cannot be blank")
                request.password.isBlank() -> ValidationResult.Invalid("Password cannot be blank")
                else -> ValidationResult.Valid
            }
        }
    }
    routing {
        authRoutes(AuthService(), JwtService(config))
    }
}

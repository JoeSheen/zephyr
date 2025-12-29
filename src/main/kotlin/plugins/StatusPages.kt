package com.shoejs.plugins

import com.shoejs.infrastructure.security.AuthorizationException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            if (cause is AuthorizationException) {
                call.respond(message = "403: ${cause.message}", status = HttpStatusCode.Forbidden)
            } else if (cause is IllegalArgumentException) {
                call.respond(message = "400: ${cause.message}", status = HttpStatusCode.BadRequest)
            }
        }
    }
}

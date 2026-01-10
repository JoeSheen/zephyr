package com.shoejs.plugins

import com.shoejs.features.auth.AuthenticationFailedException
import com.shoejs.features.auth.refresh.RefreshTokenRetrievalException
import com.shoejs.infrastructure.security.AuthorizationException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.requestvalidation.RequestValidationException
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond

fun Application.configureStatusPages() {
    install(plugin = StatusPages) {
        exception<AuthorizationException> { call, cause ->
            call.respond(status = HttpStatusCode.Unauthorized, message = mapOf("error" to cause.message))
        }
        exception<AuthenticationFailedException> { call, cause ->
            call.respond(status = HttpStatusCode.Unauthorized, message = mapOf("error" to cause.message))
        }
        exception<RefreshTokenRetrievalException> { call, cause ->
            call.respond(status = HttpStatusCode.Unauthorized, message = mapOf("error" to cause.message))
        }
        exception<RequestValidationException> { call, cause ->
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = mapOf("error" to "Validation failed: ${cause.reasons.joinToString(".")}")
            )
        }
    }
}

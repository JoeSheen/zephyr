package com.shoejs.plugins

import com.shoejs.infrastructure.security.AuthorizationException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import kotlinx.serialization.Serializable

@Serializable
data class StatusPageResponse(
    val statusCode: Int,
    val message: String
)

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            if (cause is AuthorizationException) {
                val status = HttpStatusCode.Unauthorized

                val response = StatusPageResponse(
                    status.value, "${cause.message}"
                )

                call.respond(status, response)
            }
        }
    }
}

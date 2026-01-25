package com.shoejs.plugins

import com.shoejs.common.pagination.PaginationInvalidException
import com.shoejs.features.auth.AuthenticationFailedException
import com.shoejs.features.auth.AuthenticationFieldFormatException
import com.shoejs.features.auth.AuthenticationPersistenceException
import com.shoejs.features.auth.refresh.RefreshTokenRetrievalException
import com.shoejs.infrastructure.database.DatabaseErrorCode
import com.shoejs.infrastructure.security.AuthorizationException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.requestvalidation.RequestValidationException
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import org.jetbrains.exposed.exceptions.ExposedSQLException

fun Application.configureStatusPages() {
    install(plugin = StatusPages) {
        exception<AuthorizationException> { call, cause ->
            call.respond(status = HttpStatusCode.Unauthorized, message = mapOf("error" to cause.message))
        }
        exception<AuthenticationFailedException> { call, cause ->
            call.respond(status = HttpStatusCode.Unauthorized, message = mapOf("error" to cause.message))
        }
        exception<AuthenticationFieldFormatException> { call, cause ->
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = mapOf(
                    "error" to cause.message,
                    "field" to cause.getFieldName()
                )
            )
        }
        exception<AuthenticationPersistenceException> { call, cause ->
            call.respond(status = HttpStatusCode.InternalServerError, message = mapOf("error" to cause.message))
        }
        exception<RefreshTokenRetrievalException> { call, cause ->
            call.respond(status = HttpStatusCode.Unauthorized, message = mapOf("error" to cause.message))
        }
        exception<PaginationInvalidException> { call, cause ->
            call.respond(status = HttpStatusCode.BadRequest, message = mapOf("error" to cause.message))
        }
        exception<RequestValidationException> { call, cause ->
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = mapOf("error" to "Validation failed: ${cause.reasons.joinToString(".")}")
            )
        }
        exception<ExposedSQLException> { call, cause ->
            if (cause.sqlState == DatabaseErrorCode.UNIQUE_CONSTRAINT_VIOLATION.code) {
                val columnName = cause.message?.split("Key (", ")=")?.getOrNull(1) ?: ""
                call.respond(
                    status = HttpStatusCode.Conflict,
                    message = mapOf("error" to "$columnName value violates unique constraint")
                )
            } else {
                call.respond(status = HttpStatusCode.InternalServerError, message = mapOf("error" to cause.message))
            }
        }
    }
}

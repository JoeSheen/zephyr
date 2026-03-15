package com.shoejs.infrastructure.security

import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal

fun ApplicationCall.checkUserIdentity(pathParam: String = "userId"): Long {
    val userIdClaim = this.getUserIdentity(pathParam)

    val userId = parameters[pathParam]?.toLong()
        ?: throw AuthorizationException("Path parameter '$pathParam' missing in request parameters")

    requireAuthorization(userIdClaim == userId) {
        "Expected user ID to be '$userIdClaim' but was '$userId'"
    }

    return userId
}

fun ApplicationCall.getUserIdentity(pathParam: String = "userId"): Long {
    val principal = principal<JWTPrincipal>()
        ?: throw AuthorizationException("Invalid JWT principal")
    return principal.payload.getClaim(pathParam)?.asLong()
        ?: throw AuthorizationException("JWT does not contain '$pathParam' claim")
}

private inline fun requireAuthorization(condition: Boolean, lazyMessage: () -> String) {
    if (!condition) throw AuthorizationException(lazyMessage())
}

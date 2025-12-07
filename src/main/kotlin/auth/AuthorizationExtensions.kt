package com.shoejs.auth

import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal

fun ApplicationCall.checkUserIdentity(pathParam: String = "userId"): Long {
    val principal = principal<JWTPrincipal>()
        ?: throw Exception("Invalid JWT principal")

    val userIdClaim = principal.payload.getClaim(pathParam)?.asLong()
        ?: throw Exception("JWT does not contain '$pathParam' claim")

    val userId = parameters[pathParam]?.toLong()
        ?: throw Exception("Path parameter '$pathParam' missing in request parameters")

    require(userIdClaim == userId) {
        "Expected user ID to be '$userIdClaim' but was '$userId'"
    }

    return userId
}

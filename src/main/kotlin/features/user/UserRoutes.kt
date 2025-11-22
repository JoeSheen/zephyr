package com.shoejs.features.user

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Route.userRoutes(userService: UserService) {
    route("/users") {
        authenticate("jwt-auth") {
            get("/{userId}") {
                val principal = call.principal<JWTPrincipal>()!!
                val userIdClaim = principal.payload.getClaim("userId").asLong()

                val userId = call.parameters["userId"]?.toLong() ?: return@get call.respond(
                    HttpStatusCode.BadRequest, "Path parameter 'userId' missing in request"
                )

                if (userIdClaim != userId) {
                    return@get call.respond(
                        HttpStatusCode.Forbidden, "You are not allowed to view this user's details"
                    )
                }

                userService.getUserById(userId)?.let { userDetailsResponse ->
                    call.respond(HttpStatusCode.OK, userDetailsResponse)
                } ?: call.respond(HttpStatusCode.NotFound, "User not found")
            }
            put("/{userId}") {
                val principal = call.principal<JWTPrincipal>()!!
            }
        }
    }
}

package com.shoejs.features.user

import com.shoejs.auth.checkUserIdentity
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Route.userRoutes(userService: UserService) {
    route("/users") {
        authenticate("jwt-auth") {
            get("/{userId}") {
                val userId = call.checkUserIdentity()

                userService.getUserById(userId)?.let { userDetailsResponse ->
                    call.respond(HttpStatusCode.OK, userDetailsResponse)
                } ?: call.respond(HttpStatusCode.NotFound)
            }
            put("/{userId}") {
                val userId = call.checkUserIdentity()
            }
        }
    }
}

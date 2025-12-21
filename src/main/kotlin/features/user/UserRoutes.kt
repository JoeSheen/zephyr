package com.shoejs.features.user

import com.shoejs.auth.checkUserIdentity
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Route.userRoutes(userService: UserService) {
    route("/users") {
        authenticate("jwt-auth") {
            get("/{userId}") {
                val userId = call.checkUserIdentity()

                val user = userService.getUserById(userId) ?: return@get call.respond(
                    HttpStatusCode.NotFound, "User not found"
                )

                call.respond(HttpStatusCode.OK, user)
            }
            put("/{userId}") {
                val userId = call.checkUserIdentity()
                val userUpdateRequest = call.receive<UserUpdateRequest>()

                val user = userService.updateUser(userId, userUpdateRequest) ?: return@put call.respond(
                    HttpStatusCode.NotFound, "User not found"
                )

                call.respond(HttpStatusCode.OK, user)
            }
            delete("/{userId}") {
                val userId = call.checkUserIdentity()

                when(userService.deleteUserById(userId)) {
                    true -> call.respond(HttpStatusCode.OK, "User successfully deleted")
                    false -> call.respond(HttpStatusCode.NotFound, "User not found")
                }
            }
        }
    }
}

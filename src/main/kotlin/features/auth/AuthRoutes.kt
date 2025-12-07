package com.shoejs.features.auth

import com.shoejs.auth.JwtService
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.authRoutes(authService: AuthService, jwtService: JwtService) {
    route("/auth") {
        post("/register") {
            val registerRequest = call.receive<RegisterRequest>()

            authService.registerUser(registerRequest)?.let { user ->
                val token = jwtService.generateAuthToken(user.username, user.id)
                call.respond(HttpStatusCode.Created, AuthResponse(token, user))
            } ?: call.respond(HttpStatusCode.BadRequest, "Invalid registration request")
        }
        post("/login") {
            val loginRequest = call.receive<LoginRequest>()

            authService.loginUser(loginRequest)?.let { user ->
                val token = jwtService.generateAuthToken(user.username, user.id)
                call.respond(HttpStatusCode.OK, AuthResponse(token, user))
            } ?: call.respond(HttpStatusCode.Unauthorized, "Invalid username or password")
        }
    }
}

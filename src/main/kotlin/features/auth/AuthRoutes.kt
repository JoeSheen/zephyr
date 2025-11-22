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

            val userResponse = authService.registerUser(registerRequest)
            if (userResponse == null) {
                return@post call.respond(HttpStatusCode.Unauthorized, "Invalid username or password")
            }

            val token = jwtService.generateAuthToken(userResponse.username, userResponse.id)
            call.respond(HttpStatusCode.Created, AuthResponse(token = token, user = userResponse))
        }
        post("/login") {
            val loginRequest = call.receive<LoginRequest>()

            val userResponse = authService.loginUser(loginRequest)
            if (userResponse == null) {
                return@post call.respond(HttpStatusCode.Unauthorized, "Invalid username or password")
            }

            val token = jwtService.generateAuthToken(userResponse.username, userResponse.id)
            call.respond(HttpStatusCode.OK, AuthResponse(token = token, user = userResponse))
        }
    }
}


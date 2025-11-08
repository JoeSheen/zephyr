package com.shoejs.features.auth

import com.shoejs.auth.JwtService
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.authRoutes(authService: AuthService, jwtService: JwtService) {
    route("/auth") {
        // ----
        post("/register") {

        }
        // ----
        post("/login") {
            val loginRequest = call.receive<LoginRequest>()

            val isValid = authService.validateLogin(loginRequest)
            if (!isValid) {
                return@post call.respond(HttpStatusCode.Unauthorized, "Invalid username or password")
            }

            val authToken = jwtService.generateAuthToken()
            call.respondText(authToken, ContentType.Application.Json, HttpStatusCode.OK)
        }
        // --- TEMP ---
        get("/temp") {
            call.respond(jwtService.generateAuthToken())
        }
    }
}

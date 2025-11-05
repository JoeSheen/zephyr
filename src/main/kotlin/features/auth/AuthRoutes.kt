package com.shoejs.features.auth

import com.shoejs.auth.JwtService
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.authRoutes(jwtService: JwtService) {
    route("/auth") {
        // ----
        post("/register") {

        }
        // ----
        post("/login") {

        }
        // --- TEMP ---
        get("/temp") {
            call.respond(jwtService.generateAuthToken())
        }
    }
    /*
    AuthRepository/Service or UserRepository/Service?
     */
}
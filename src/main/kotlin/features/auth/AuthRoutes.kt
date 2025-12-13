package com.shoejs.features.auth

import com.shoejs.auth.JwtService
import com.shoejs.features.auth.refresh.RefreshToken
import com.shoejs.features.auth.refresh.RefreshTokenService
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.authRoutes(
    authService: AuthService, jwtService: JwtService, refreshTokenService: RefreshTokenService
) {
    route("/auth") {
        post("/register") {
            val registerRequest = call.receive<RegisterRequest>()

            val user = authService.registerUser(registerRequest) ?: return@post call.respond(
                HttpStatusCode.BadRequest,
                "Invalid registration request"
            )

            val accessToken = jwtService.generateAuthToken(user.username, user.id)
            val refreshCookie = refreshTokenService.storeRefreshToken(RefreshToken(user.id))

            call.response.cookies.append(refreshCookie)
            call.respond(HttpStatusCode.Created, AuthResponse(accessToken, user))
        }
        post("/login") {
            val loginRequest = call.receive<LoginRequest>()

            val user = authService.loginUser(loginRequest) ?: return@post call.respond(
                HttpStatusCode.Unauthorized,
                "Invalid username or password"
            )

            val accessToken = jwtService.generateAuthToken(user.username, user.id)
            val refreshCookie = refreshTokenService.storeRefreshToken(RefreshToken(user.id))

            call.response.cookies.append(refreshCookie)
            call.respond(HttpStatusCode.OK, AuthResponse(accessToken, user))
        }
    }
}

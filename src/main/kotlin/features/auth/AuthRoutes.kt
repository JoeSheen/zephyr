package com.shoejs.features.auth

import com.shoejs.infrastructure.security.JwtService
import com.shoejs.features.auth.refresh.ExpiredRefreshToken
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

            val user = authService.registerUser(registerRequest)

            val accessToken = jwtService.generateAuthToken(user.username, user.id)
            val refreshToken = refreshTokenService.createAndStoreRefreshToken(user.id, user.username)

            call.response.cookies.append(refreshToken.toCookie())
            call.respond(HttpStatusCode.Created, AuthResponse(accessToken, user))
        }
        post("/login") {
            val loginRequest = call.receive<LoginRequest>()

            val user = authService.loginUser(loginRequest)

            val accessToken = jwtService.generateAuthToken(user.username, user.id)
            val refreshToken = refreshTokenService.createAndStoreRefreshToken(user.id, user.username)

            call.response.cookies.append(refreshToken.toCookie())
            call.respond(HttpStatusCode.OK, AuthResponse(accessToken, user))
        }
        post("/refresh/token") {
            val refreshToken = call.request.cookies["refresh_token"] ?: return@post call.respond(
                HttpStatusCode.Unauthorized, "No refresh token provided"
            )

            val (userId, username) = refreshTokenService.getAndRevokeRefreshToken(refreshToken)

            val accessToken = jwtService.generateAuthToken(username, userId)
            val newRefreshToken = refreshTokenService.createAndStoreRefreshToken(userId, username)

            call.response.cookies.append(newRefreshToken.toCookie())
            call.respond(HttpStatusCode.OK, AuthRefreshResponse(accessToken))
        }
        post("/logout") {
            val refreshToken = call.request.cookies["refresh_token"] ?: return@post call.respond(
                HttpStatusCode.Unauthorized, "No refresh token provided"
            )

            refreshTokenService.deleteRefreshToken(refreshToken)

            call.response.cookies.append(ExpiredRefreshToken().toCookie())
            call.respond(HttpStatusCode.OK, "Successfully logged out")
        }
    }
}

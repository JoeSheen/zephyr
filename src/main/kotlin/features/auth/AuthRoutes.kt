package com.shoejs.features.auth

import com.shoejs.auth.JwtService
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.authRoutes(authService: AuthService, jwtService: JwtService) {
    route(path = AuthConfig.AUTH_PATH_PREFIX) {
        post(path = AuthConfig.REGISTER_PATH_SUFFIX) {
            call.receive<RegisterRequest>().let { request ->
                authService.registerUser(request)
            }?.let { user ->
                val token = jwtService.generateAuthToken(user.username, user.id)
                call.respond(HttpStatusCode.Created, AuthResponse(token, user))
            } ?: call.respond(HttpStatusCode.BadRequest, AuthConfig.INVALID_REGISTRATION_RESPONSE)
        }
        post(path = AuthConfig.LOGIN_PATH_SUFFIX) {
            call.receive<LoginRequest>().let { request ->
                authService.loginUser(request)
            }?.let { user ->
                val token = jwtService.generateAuthToken(user.username, user.id)
                call.respond(HttpStatusCode.OK, AuthResponse(token, user))
            } ?: call.respond(HttpStatusCode.Unauthorized, AuthConfig.INVALID_LOGIN_RESPONSE)
        }
    }
}

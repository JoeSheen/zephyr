package com.shoejs.plugins

import com.shoejs.auth.JwtConfig
import com.shoejs.auth.JwtService
import com.shoejs.features.auth.AuthService
import com.shoejs.features.auth.LoginRequest
import com.shoejs.features.auth.RegisterRequest
import com.shoejs.features.auth.authRoutes
import com.shoejs.features.tag.TagRequest
import com.shoejs.features.tag.TagService
import com.shoejs.features.tag.tagRoutes
import com.shoejs.features.user.UserService
import com.shoejs.features.user.userRoutes
import com.shoejs.utils.isHexColor
import com.shoejs.utils.isInvalidEmail
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.requestvalidation.RequestValidation
import io.ktor.server.plugins.requestvalidation.ValidationResult
import io.ktor.server.routing.routing

fun Application.configureRouting(config: JwtConfig) {
    install(RequestValidation) {
        validate<RegisterRequest> { request ->
            when {
                request.firstName.isBlank() -> ValidationResult.Invalid("First name cannot be blank")
                request.lastName.isBlank() -> ValidationResult.Invalid("Last name cannot be blank")
                request.dateOfBirth.isBlank() -> ValidationResult.Invalid("Date of birth cannot be blank")
                request.username.isBlank() -> ValidationResult.Invalid("Username cannot be blank")
                isInvalidEmail(request.email) -> ValidationResult.Invalid("Invalid email format")
                request.password.isBlank() -> ValidationResult.Invalid("Password cannot be blank")
                else -> ValidationResult.Valid
            }
        }
        validate<LoginRequest> { request ->
            when {
                request.username.isBlank() -> ValidationResult.Invalid("Username cannot be blank")
                request.password.isBlank() -> ValidationResult.Invalid("Password cannot be blank")
                else -> ValidationResult.Valid
            }
        }
        validate<TagRequest> { request ->
            when {
                request.name.isBlank() -> ValidationResult.Invalid("Tag name cannot be blank")
                !isHexColor(request.hexColor) -> ValidationResult.Invalid("Invalid hex color")
                else -> ValidationResult.Valid
            }
        }
    }
    routing {
        authRoutes(AuthService(), JwtService(config))
        tagRoutes(TagService())
        userRoutes(UserService())
    }
}

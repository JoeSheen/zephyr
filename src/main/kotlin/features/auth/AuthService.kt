package com.shoejs.features.auth

import com.shoejs.features.user.UserRepository
import com.shoejs.features.user.UserResponse
import com.shoejs.features.user.toUserResponse
import org.mindrot.jbcrypt.BCrypt
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class AuthService {

    private val logger: Logger = LoggerFactory.getLogger(AuthService::class.java)

    fun registerUser(registerRequest: RegisterRequest): UserResponse {
        val (firstName, lastName, dateOfBirthStr, username, email, password) = registerRequest

        val dateOfBirth = try {
            LocalDate.parse(dateOfBirthStr, DateTimeFormatter.ISO_DATE)
        } catch (e: DateTimeParseException) {
            logger.error("Error parsing date ", e)
            throw AuthenticationFieldFormatException(
                "Date of birth is invalid or in the wrong format",
                e,
                "dateOfBirth"
            )
        }

        return UserRepository.createUser(
            firstName = firstName,
            lastName = lastName,
            dateOfBirth = dateOfBirth,
            username = username,
            email = email,
            password = BCrypt.hashpw(password, BCrypt.gensalt())
        )?.toUserResponse() ?: throw AuthenticationPersistenceException()
    }

    fun loginUser(loginRequest: LoginRequest): UserResponse {
        val user = UserRepository.getUserByUsername(username = loginRequest.username)
            ?: throw AuthenticationFailedException()

        if(!BCrypt.checkpw(loginRequest.password, user.password))
            throw AuthenticationFailedException()

        return user.toUserResponse()
    }
}

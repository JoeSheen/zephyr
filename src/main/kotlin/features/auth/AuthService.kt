package com.shoejs.features.auth

import com.shoejs.features.user.UserRepository
import com.shoejs.features.user.UserResponse
import com.shoejs.features.user.toUserResponse
import org.mindrot.jbcrypt.BCrypt
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class AuthService {

    fun registerUser(registerRequest: RegisterRequest): UserResponse? {
        val (firstName, lastName, dateOfBirthStr, username, email, password) = registerRequest

        val dateOfBirth = try {
            LocalDate.parse(dateOfBirthStr, DateTimeFormatter.ISO_DATE)
        } catch (e: DateTimeParseException) {
            // TODO: impl custom exception
            throw Exception(e.message)
        }

        val passwordHash = BCrypt.hashpw(password, BCrypt.gensalt())

        return UserRepository.createUser(
            firstName = firstName,
            lastName = lastName,
            dateOfBirth = dateOfBirth,
            username = username,
            email = email,
            password = passwordHash
        )?.toUserResponse()
    }

    fun loginUser(loginRequest: LoginRequest): UserResponse? {
        val (username, password) = loginRequest

        val user = UserRepository.getUserByUsername(username)
            ?: return null
        // TODO: throw exception?

        val match = BCrypt.checkpw(password, user.password)
        if (!match) {
            return null
        }
        return user.toUserResponse()
    }
}

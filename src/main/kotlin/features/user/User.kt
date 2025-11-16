package com.shoejs.features.user

import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

data class User(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: LocalDate,
    val username: String,
    val email: String,
    val password: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
)

@Serializable
data class UserResponse(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val username: String,
)

fun User.toUserResponse() = UserResponse(
    id = this.id,
    firstName = this.firstName,
    lastName = this.lastName,
    username = this.username
)

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
    val phoneNumber: String?,
    val password: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
)

@Serializable
data class UserUpdateRequest(
    val username: String?,
    val email: String?,
    val phoneNumber: String?,
    val countryCode: String?
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

@Serializable
data class UserDetailsResponse(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: String,
    val username: String,
    val email: String,
    val phoneNumber: String?,
    val createdAt: String,
    val updatedAt: String?
)

fun User.toUserDetailsResponse() = UserDetailsResponse(
    id = this.id,
    firstName = this.firstName,
    lastName = this.lastName,
    dateOfBirth = this.dateOfBirth.toString(),
    username = this.username,
    email = this.email,
    phoneNumber = this.phoneNumber,
    createdAt = this.createdAt.toString(),
    updatedAt = this.updatedAt?.toString()
)

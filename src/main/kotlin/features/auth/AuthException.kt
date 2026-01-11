package com.shoejs.features.auth

sealed class AuthenticationException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

class AuthenticationFailedException(message: String = "Invalid username or password") : AuthenticationException(message)

class AuthenticationFieldFormatException(message: String, cause: Throwable, private val fieldName: String) :
    AuthenticationException(message, cause) {
        fun getFieldName(): String = fieldName
}

class AuthenticationPersistenceException(message: String = "Failed to persist user registration data") :
    AuthenticationException(message)

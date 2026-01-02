package com.shoejs.features.auth

sealed class AuthenticationException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

class AuthenticationFailedException(message: String = "Invalid username or password") : AuthenticationException(message)

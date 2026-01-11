package com.shoejs.features.auth.refresh

sealed class RefreshTokenException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

class RefreshTokenRetrievalException(message: String) : RefreshTokenException(message)

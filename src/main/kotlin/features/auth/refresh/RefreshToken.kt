package com.shoejs.features.auth.refresh

import java.util.UUID

data class RefreshToken(
    val userId: Long,
    val value: String = UUID.randomUUID().toString(),
    val expiration: Long = 1209600,
    val revoked: Boolean = false
)

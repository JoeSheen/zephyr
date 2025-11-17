package com.shoejs.auth

data class JwtConfig(
    val realm: String,
    val secret: String,
    val audience: String,
    val domain: String,
    val expirationOffset: Long,
    val notBeforeOffset: Long,
)

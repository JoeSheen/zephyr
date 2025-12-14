package com.shoejs.features.auth.refresh

import io.ktor.http.Cookie
import java.util.UUID

data class RefreshToken(
    val userId: Long,
    val key: String = UUID.randomUUID().toString(),
    val expiration: Long = 1209600
) {
    fun toCookie(): Cookie = Cookie(
        name = "refresh_token",
        value = this.key,
        httpOnly = false, // Change to true
        secure = true,    // Change to true
        path = "/",
        maxAge = this.expiration.toInt()
    )
}

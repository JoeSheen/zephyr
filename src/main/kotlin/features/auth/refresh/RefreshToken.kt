package com.shoejs.features.auth.refresh

import io.ktor.http.Cookie
import java.util.UUID

interface BaseToken {
    val key: String
    val expiration: Long

    fun toCookie(): Cookie = Cookie(
        name = "refresh_token",
        value = this.key,
        httpOnly = false,  // <- Change to true!
        secure = false,    // <- Change to true!
        path = "/",
        maxAge = this.expiration.toInt()
    )
}

data class RefreshToken(
    val userId: Long,
    override val key: String = UUID.randomUUID().toString(),
    override val expiration: Long = 1209600
) : BaseToken

data class ExpiredRefreshToken(
    override val key: String = "",
    override val expiration: Long = 0
) : BaseToken

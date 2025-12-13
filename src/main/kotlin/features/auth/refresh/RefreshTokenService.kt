package com.shoejs.features.auth.refresh

import io.ktor.http.Cookie

class RefreshTokenService {

    suspend fun storeRefreshToken(refreshToken: RefreshToken): Cookie {
        RefreshTokenRepository.storeRefreshToken(refreshToken.userId, refreshToken.value, refreshToken.expiration)
        return Cookie(
            name = "refresh_token",
            value = refreshToken.value,
            httpOnly = false, // Change to true
            secure = true, // Change to false
            path = "/",
            maxAge = refreshToken.expiration.toInt()
        )
    }

    suspend fun getRefreshTokenByUserId(userId: Long): String {
        return RefreshTokenRepository.getRefreshToken(userId)
    }

    suspend fun deleteRefreshTokenByUserId(userId: Long) {
        RefreshTokenRepository.deleteRefreshToken(userId)
    }
}

package com.shoejs.features.auth.refresh

class RefreshTokenService {

    suspend fun createAndStoreRefreshToken(userId: Long, username: String): RefreshToken {
        val refreshToken = RefreshToken("${userId}:${username}")
        RefreshTokenRepository.storeRefreshTokenValue(
            refreshToken.key, refreshToken.expiration, refreshToken.userValue
        )
        return refreshToken
    }

    suspend fun getAndRevokeRefreshToken(refreshTokenKey: String): Pair<Long, String>? {
        return RefreshTokenRepository.getAndRevokeRefreshTokenValue(refreshTokenKey)
            ?.split(":", limit = 2)?.let { (num, str) -> num.toLong() to str }
    }

    suspend fun deleteRefreshToken(refreshTokenKey: String) {
        RefreshTokenRepository.deleteRefreshTokenValue(refreshTokenKey)
    }
}

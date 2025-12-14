package com.shoejs.features.auth.refresh

class RefreshTokenService {

    suspend fun createAndStoreRefreshToken(userId: Long): RefreshToken {
        val refreshToken = RefreshToken(userId)
        RefreshTokenRepository.storeRefreshTokenValue(
            refreshToken.key, refreshToken.expiration, refreshToken.userId
        )
        return refreshToken
    }

    suspend fun getRefreshTokenValue(refreshTokenKey: String): Long {
        return RefreshTokenRepository.getRefreshTokenValue(refreshTokenKey)
    }

    suspend fun deleteRefreshTokenValue(refreshTokenKey: String) {
        RefreshTokenRepository.deleteRefreshTokenValue(refreshTokenKey)
    }
}

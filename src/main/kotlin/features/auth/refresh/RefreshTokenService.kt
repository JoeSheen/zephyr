package com.shoejs.features.auth.refresh

class RefreshTokenService {

    suspend fun createAndStoreRefreshToken(userId: Long, username: String): RefreshToken {
        val refreshToken = RefreshToken("${userId}:${username}")
        RefreshTokenRepository.storeRefreshTokenValue(
            refreshToken.key, refreshToken.expiration, refreshToken.userValue
        )
        return refreshToken
    }

    suspend fun getAndRevokeRefreshToken(refreshTokenKey: String): Pair<Long, String> {
        return RefreshTokenRepository.getAndRevokeRefreshTokenValue(refreshTokenKey)?.toRefreshTokenPair()
            ?: throw RefreshTokenRetrievalException("Refresh token expired or revoked")
    }

    suspend fun deleteRefreshToken(refreshTokenKey: String) {
        RefreshTokenRepository.deleteRefreshTokenValue(refreshTokenKey)
    }
}

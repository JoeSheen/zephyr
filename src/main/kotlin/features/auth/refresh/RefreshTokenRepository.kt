package com.shoejs.features.auth.refresh

import com.shoejs.infrastructure.redis.RedisConnectionManager
import io.lettuce.core.ExperimentalLettuceCoroutinesApi
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@OptIn(ExperimentalLettuceCoroutinesApi::class)
object RefreshTokenRepository {

    private val logger: Logger = LoggerFactory.getLogger(RefreshTokenRepository::class.java)

    private val redis get() = RedisConnectionManager.commands

    private const val REFRESH_PREFIX = "rft_:"

    suspend fun storeRefreshTokenValue(refreshTokenKey: String, expirationSeconds: Long, userValue: String) {
        val value = redis.setex("$REFRESH_PREFIX$refreshTokenKey", expirationSeconds, userValue)
        logger.info("Stored value: [$userValue] for key: [$REFRESH_PREFIX$refreshTokenKey] -> $value")
    }

    suspend fun getAndRevokeRefreshTokenValue(refreshTokenKey: String): String? {
        val value = redis.getdel("$REFRESH_PREFIX$refreshTokenKey")
        logger.info("Retrieved value: [$value] for key: [$REFRESH_PREFIX$refreshTokenKey]")
        return value
    }

    suspend fun deleteRefreshTokenValue(refreshTokenKey: String) {
        val value = redis.del("$REFRESH_PREFIX$refreshTokenKey")
        logger.info("Deleted refresh token value count: [$value]")
    }
}

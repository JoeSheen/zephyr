package com.shoejs.features.auth.refresh

import com.shoejs.infrastructure.redis.RedisConnectionManager
import io.lettuce.core.ExperimentalLettuceCoroutinesApi
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@OptIn(ExperimentalLettuceCoroutinesApi::class)
object RefreshTokenRepository {

    private val logger: Logger = LoggerFactory.getLogger(RefreshTokenRepository::class.java)

    private val redis get() = RedisConnectionManager.commands

    suspend fun storeRefreshTokenValue(refreshTokenKey: String, expiration: Long, userId: Long) {
        val value = redis.setex(refreshTokenKey, expiration, userId.toString())
        logger.info("Stored value: [$userId] for key [$refreshTokenKey] - $value")
    }

    suspend fun getRefreshTokenValue(refreshTokenKey: String): Long {
        val value = redis.get(refreshTokenKey)?.toLong() ?: -1L
        logger.info("Retrieved value: [$value] for key [$refreshTokenKey]")
        return value
    }

    suspend fun deleteRefreshTokenValue(refreshTokenKey: String) {
        val value = redis.del(refreshTokenKey)
        logger.info("Deleted refresh token value count: [$value]")
        // Should return a Boolean?
    }
}

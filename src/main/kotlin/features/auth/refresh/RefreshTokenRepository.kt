package com.shoejs.features.auth.refresh

import com.shoejs.infrastructure.redis.RedisConnectionManager
import io.lettuce.core.ExperimentalLettuceCoroutinesApi
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.UUID

@OptIn(ExperimentalLettuceCoroutinesApi::class)
object RefreshTokenRepository {

    private val logger: Logger = LoggerFactory.getLogger(RefreshTokenRepository::class.java)

    private val redis get() = RedisConnectionManager.commands

    private val KEY_ID = UUID.fromString("453ddf64-adee-4b88-8fb9-f665dd387b56")

    suspend fun storeRefreshToken(userId: Long, refreshTokenValue: String, expiration: Long) {
        val value = redis.setex("$KEY_ID:$userId", expiration, refreshTokenValue)
        logger.info("Stored refresh token: $value")
    }

    suspend fun getRefreshToken(userId: Long): String {
        val value = redis.get("$KEY_ID:$userId")
            ?: throw IllegalStateException("Refresh token not found")
        logger.info("Retrieved refresh token: $value")
        return value
    }

    suspend fun deleteRefreshToken(userId: Long) {
        val value = redis.del("$KEY_ID:$userId")
        logger.info("Deleted refresh token count: $value")
    }
}

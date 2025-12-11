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

    suspend fun saveRefreshToken(userId: Long, refreshToken: String) {
        val result = redis.setex("$KEY_ID:$userId", 60L, refreshToken) // TODO: <- placeholders
        logger.info("$result")
    }

}
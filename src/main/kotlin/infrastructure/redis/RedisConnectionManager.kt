package com.shoejs.infrastructure.redis

import com.shoejs.config.RedisConfig
import io.lettuce.core.ExperimentalLettuceCoroutinesApi
import io.lettuce.core.RedisClient
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.api.coroutines
import io.lettuce.core.api.coroutines.RedisCoroutinesCommands
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@OptIn(ExperimentalLettuceCoroutinesApi::class)
object RedisConnectionManager {

    private val logger: Logger = LoggerFactory.getLogger(RedisConnectionManager::class.java)

    private lateinit var redisClient: RedisClient

    private lateinit var connection: StatefulRedisConnection<String, String>

    lateinit var commands: RedisCoroutinesCommands<String, String>
        private set

    fun connect(config: RedisConfig) {
        redisClient = RedisClient.create("redis://${config.host}:${config.port}")
        connection = redisClient.connect()
        commands = connection.coroutines()
        logger.info("Connected to redis cache at redis://${config.host}:${config.port}")
    }

    fun close() {
        connection.close()
        redisClient.shutdown()
        logger.info("Connection to redis cache closed")
    }
}

package com.shoejs.infrastructure.redis

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

    fun connect(host: String, port: Int) {
        redisClient = RedisClient.create("redis://$host:$port")
        connection = redisClient.connect()
        commands = connection.coroutines()
        logger.info("Connected to redis cache at redis://$host:$port")
    }

    fun close() {
        connection.close()
        redisClient.shutdown()
        logger.info("Connection to redis cache closed")
    }
}

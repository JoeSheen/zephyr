package com.shoejs.infrastructure.redis

import org.slf4j.Logger
import org.slf4j.LoggerFactory

object RedisConnectionManager {

    private val logger: Logger = LoggerFactory.getLogger(RedisConnectionManager::class.java)

    fun connect(host: String, port: Int) {
        // Redis connection setup
        logger.info("Connected to redis cache at redis://$host:$port")
    }

    fun close() {
        // Close Redis connection and perform any clean-up
        logger.info("Connection closed")
    }
}

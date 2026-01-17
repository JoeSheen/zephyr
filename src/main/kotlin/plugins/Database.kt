package com.shoejs.plugins

import com.shoejs.config.DatabaseConfig
import com.shoejs.infrastructure.database.DatabaseInitializer
import io.ktor.server.application.Application

fun Application.configureDatabases() {
    val config = DatabaseConfig.fromEnvironment(environment)

    DatabaseInitializer.init(config)
}

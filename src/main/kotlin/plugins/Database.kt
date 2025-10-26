package com.shoejs.plugins

import com.shoejs.database.DatabaseFactory
import io.ktor.server.application.Application

fun Application.configureDatabases() {
    val url = environment.config.property("postgres.url").getString()
    val driver = environment.config.property("postgres.driver").getString()
    val user = environment.config.property("postgres.user").getString()
    val password = environment.config.property("postgres.password").getString()

    DatabaseFactory.init(
        url = url,
        driver = driver,
        user = user,
        password = password
    )
}

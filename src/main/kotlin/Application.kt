package com.shoejs

import com.shoejs.auth.JwtConfig
import com.shoejs.plugins.configureDatabases
import com.shoejs.plugins.configureRouting
import com.shoejs.plugins.configureSecurity
import com.shoejs.plugins.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val jwtConfig = JwtConfig(
        realm = environment.config.property("jwt.realm").getString(),
        secret = environment.config.property("jwt.secret").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        domain = environment.config.property("jwt.domain").getString(),
        expirationOffset = 5_400_000,
        notBeforeOffset = 30_000
    )

    configureHTTP()
    configureSecurity(jwtConfig)
    configureSerialization()
    configureDatabases()
    configureRouting(jwtConfig)
}

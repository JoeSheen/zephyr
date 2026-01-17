package com.shoejs

import com.shoejs.infrastructure.security.JwtConfig
import com.shoejs.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val jwtConfig = JwtConfig.fromAppConfig(environment.config)

    configureSecurity(jwtConfig)
    configureSerialization()
    configureDatabases()
    configureRouting(jwtConfig)
    configureSwagger()
    configureCache()
    configureStatusPages()
    configureDefaultHeaders()
    configureCors()
}

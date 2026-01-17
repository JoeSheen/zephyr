package com.shoejs

import com.shoejs.config.JwtConfig
import com.shoejs.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val jwtConfig = JwtConfig.fromEnvironment(environment)

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

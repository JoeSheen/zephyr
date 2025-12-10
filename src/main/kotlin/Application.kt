package com.shoejs

import com.shoejs.auth.JwtConfig
import com.shoejs.plugins.configureDatabases
import com.shoejs.plugins.configureRouting
import com.shoejs.plugins.configureSecurity
import com.shoejs.plugins.configureSerialization
import com.shoejs.plugins.configureSwagger
import com.shoejs.plugins.configureCache
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val jwtConfig = JwtConfig.fromAppConfig(environment.config)

    configureHTTP()
    configureSecurity(jwtConfig)
    configureSerialization()
    configureDatabases()
    configureRouting(jwtConfig)
    configureSwagger()
    configureCache()
}

package com.shoejs.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.Application
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt

fun Application.configureSecurity() {
    //val secret
    //val audience
    //val issuer
    val jwtRealm = environment.config.property("jwt.realm").toString()

    authentication {
        jwt("jwt-auth") {
            realm = jwtRealm
            verifier(
                JWT.require(Algorithm.HMAC256(""))
                    .withAudience("")
                    .withIssuer("")
                .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains("")) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}
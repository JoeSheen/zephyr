package com.shoejs.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date
import java.util.UUID

class JwtService(private val config: JwtConfig) {

    fun generateAuthToken(subject: String): String {
        return JWT.create().withJWTId(UUID.randomUUID().toString())
            .withIssuer(config.domain).withSubject(subject)
            .withExpiresAt(Date(System.currentTimeMillis() + config.expirationOffset))
            .withNotBefore(Date(System.currentTimeMillis() - config.notBeforeOffset))
            .withAudience(config.audience).withIssuedAt(Date())
            .sign(Algorithm.HMAC256(config.secret))
    }

}
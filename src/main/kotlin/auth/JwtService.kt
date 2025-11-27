package com.shoejs.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date
import java.util.UUID

class JwtService(private val config: JwtConfig) {

    fun generateAuthToken(subject: String, userIdClaim: Long): String =
        JWT.create().withJWTId(UUID.randomUUID().toString()).withSubject(subject).withIssuer(config.domain)
            .withAudience(config.audience).withClaim("userId", userIdClaim)
            .withExpiresAt(Date(System.currentTimeMillis() + config.expirationOffset))
            .withNotBefore(Date(System.currentTimeMillis() - config.notBeforeOffset)).withIssuedAt(Date())
            .sign(Algorithm.HMAC256(config.secret))

}

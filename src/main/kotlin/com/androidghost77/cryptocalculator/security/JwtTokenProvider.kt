package com.androidghost77.cryptocalculator.security

import com.androidghost77.cryptocalculator.security.model.UserPrincipal
import com.androidghost77.cryptocalculator.services.Logging
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.Authentication
import java.security.Key
import java.security.SignatureException
import java.util.*

class JwtTokenProvider(
    private val jwtSecret: String,
    private val jwtExpirationInMs: Int,
) {
    fun generateToken(authentication: Authentication): String {
        val userPrincipal: UserPrincipal = authentication.principal as UserPrincipal
        val issuedAt = Date()
        val expirationDate = Date(issuedAt.time + jwtExpirationInMs)
        val keyBytes = Decoders.BASE64.decode(jwtSecret)
        val key: Key = Keys.hmacShaKeyFor(keyBytes)
        return Jwts.builder()
            .setSubject(userPrincipal.user.id)
            .setIssuedAt(issuedAt)
            .setExpiration(expirationDate)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }

    fun getUserIdFromJWT(token: String?): String {
        val keyBytes = Decoders.BASE64.decode(jwtSecret)
        val key: Key = Keys.hmacShaKeyFor(keyBytes)
        val tokenBody = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
        return tokenBody.subject
    }

    fun validateToken(token: String?): Boolean {
        try {
            Jwts.parserBuilder()
                .setSigningKey(jwtSecret.toByteArray())
                .build()
                .parseClaimsJws(token)
            return true
        } catch (exc: RuntimeException) {
            logger().error(EXCEPTION_MESSAGES[exc.javaClass], exc)
        }
        return false
    }

    companion object : Logging {
        private val EXCEPTION_MESSAGES = mapOf(
            SignatureException::class.java to "Invalid JWT signature",
            MalformedJwtException::class.java to "Invalid JWT token",
            ExpiredJwtException::class.java to "Expired JWT token",
            UnsupportedJwtException::class.java to "Unsupported JWT token",
            IllegalArgumentException::class.java to "JWT claims string is empty",
        )
    }
}

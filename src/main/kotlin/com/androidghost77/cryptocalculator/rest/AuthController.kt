package com.androidghost77.cryptocalculator.rest

import com.androidghost77.cryptocalculator.models.JwtTokenDto
import com.androidghost77.cryptocalculator.models.AuthCredentials
import com.androidghost77.cryptocalculator.security.JwtTokenProvider
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val tokenProvider: JwtTokenProvider
) {

    @PostMapping
    fun authenticate(@RequestBody user: AuthCredentials): JwtTokenDto {
        val authenticate: Authentication = authenticationManager.authenticate (
            UsernamePasswordAuthenticationToken (user.username, user.password)
        )

        return JwtTokenDto (tokenProvider.generateToken(authenticate))
    }
}
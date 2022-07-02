package com.androidghost77.cryptocalculator.models

data class JwtTokenDto(
    val token: String,
    val tokenType: String = "Bearer"
)
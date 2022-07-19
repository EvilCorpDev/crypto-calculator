package com.androidghost77.cryptocalculator.models

data class UserDto(
    val username: String,
    val password: String,
    val email: String,
    val connectedToBinance: Boolean = false,
)

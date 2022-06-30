package com.androidghost77.cryptocalculator.rest

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController {

    @PostMapping("/login")
    fun login() {
        TODO("Not yet implemented")
    }

    @PostMapping("/logout")
    fun logout() {
        TODO("Not yet implemented")
    }
}
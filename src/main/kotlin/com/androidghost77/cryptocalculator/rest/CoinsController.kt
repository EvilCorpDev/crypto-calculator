package com.androidghost77.cryptocalculator.rest

import com.androidghost77.cryptocalculator.models.MyWallet
import com.androidghost77.cryptocalculator.services.CoinsService
import com.androidghost77.cryptocalculator.services.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/coins")
class CoinsController(
    private val coinsService: CoinsService,
    private val userService: UserService,
) {

    @GetMapping("/price")
    fun getCoinAveragePrices(): MyWallet = coinsService.getCoinsAveragePrice(userService.getCurrentUser())
}
package com.androidghost77.cryptocalculator.rest

import com.androidghost77.cryptocalculator.models.CoinOperationDto
import com.androidghost77.cryptocalculator.services.CoinOperationsService
import com.androidghost77.cryptocalculator.services.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/operations")
class CoinOperationsController(
    val coinOperationsService: CoinOperationsService,
    val userService: UserService,
) {

    @PostMapping("/buy")
    fun buyCrypto(@RequestBody coin: CoinOperationDto) {
        coinOperationsService.buyCoin(coin, userService.getCurrentUser())
    }

    @PostMapping("/sell")
    fun sellCrypto(@RequestBody coin: CoinOperationDto) {
        coinOperationsService.sellCoin(coin, userService.getCurrentUser())
    }
}
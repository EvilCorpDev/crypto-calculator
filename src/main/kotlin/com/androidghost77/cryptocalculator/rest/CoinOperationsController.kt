package com.androidghost77.cryptocalculator.rest

import com.androidghost77.cryptocalculator.models.CoinOperationDto
import com.androidghost77.cryptocalculator.models.MyWallet
import com.androidghost77.cryptocalculator.services.CoinOperationsService
import com.androidghost77.cryptocalculator.services.CoinsService
import com.androidghost77.cryptocalculator.services.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/operations")
class CoinOperationsController(
    private val coinOperationsService: CoinOperationsService,
    private val coinsService: CoinsService,
    private val userService: UserService,
) {

    @PostMapping
    fun makeCoinOperation(@RequestBody coin: CoinOperationDto) {
        coinOperationsService.addCoinOperation(coin, userService.getCurrentUser())
    }

    @GetMapping("/refresh")
    fun refreshOperations(): MyWallet {
        coinOperationsService.loadLatestTransactions(userService.getCurrentUser())
        return coinsService.getCoinsAveragePrice(userService.getCurrentUser())
    }

    @GetMapping("/{symbol}")
    fun orderHistory(@PathVariable("symbol") symbol: String): List<CoinOperationDto> =
        coinOperationsService.getCoinOrderHistory(symbol)
}
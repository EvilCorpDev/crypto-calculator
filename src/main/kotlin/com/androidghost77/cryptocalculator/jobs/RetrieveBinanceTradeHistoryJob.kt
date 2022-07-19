package com.androidghost77.cryptocalculator.jobs

import com.androidghost77.cryptocalculator.repos.entities.Coin
import com.androidghost77.cryptocalculator.security.DbUserDetailsManager
import com.androidghost77.cryptocalculator.services.BinanceLoader
import com.androidghost77.cryptocalculator.services.CoinOperationsService
import com.binance.connector.client.impl.SpotClientImpl
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class RetrieveBinanceTradeHistoryJob(
    private val userDetailsManager: DbUserDetailsManager,
    private val binanceLoader: BinanceLoader,
    private val coinOperationsService: CoinOperationsService,
) {

    @Scheduled(cron = "")
    fun updateUserOrderHistory() {
        val allUsers = userDetailsManager.getAllUsers()
        allUsers.forEach {user ->
            val client = SpotClientImpl(user.binanceToken, user.secretBinanceKey)
            val coins: List<Coin> = binanceLoader.loadAccountCoins(client)
            val latestOperationTime = coinOperationsService.getLatestOperationTime(user)

            coins.forEach {
                binanceLoader.loadOperationsHistory(client, user, it.symbol, latestOperationTime)
                    .forEach { coinOperation ->
                        coinOperationsService.saveCoinOperation(coinOperation)
                    }
            }
        }
    }
}
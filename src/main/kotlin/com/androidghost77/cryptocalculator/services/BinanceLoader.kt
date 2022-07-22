package com.androidghost77.cryptocalculator.services

import com.androidghost77.cryptocalculator.repos.entities.Coin
import com.androidghost77.cryptocalculator.repos.entities.CoinOperation
import com.androidghost77.cryptocalculator.repos.entities.User
import com.binance.connector.client.SpotClient
import java.math.BigDecimal
import java.time.ZonedDateTime

interface BinanceLoader {
    fun loadAssetAndSaveCoin(client: SpotClient, symbol: String): Coin
    fun loadAccountCoins(client: SpotClient): List<Coin>
    fun loadOperationsHistory(client: SpotClient, user: User, symbol: String, startTime: Long = 0L): List<CoinOperation>
    fun loadBuyCryptoHistory(client: SpotClient, user: User, startTime: ZonedDateTime): List<CoinOperation>
    fun getAssetPrice(client: SpotClient, symbol: String, date: ZonedDateTime): BigDecimal
}
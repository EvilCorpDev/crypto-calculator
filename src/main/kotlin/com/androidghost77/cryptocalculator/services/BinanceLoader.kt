package com.androidghost77.cryptocalculator.services

import com.androidghost77.cryptocalculator.repos.entities.Coin
import com.androidghost77.cryptocalculator.repos.entities.CoinOperation
import com.androidghost77.cryptocalculator.repos.entities.User
import com.binance.connector.client.SpotClient

interface BinanceLoader {
    fun findAssetFullNameAndSaveCoin(client: SpotClient,symbol: String): String
    fun loadAccountCoins(client: SpotClient): List<Coin>
    fun loadOperationsHistory(client: SpotClient, user: User, symbol: String, startTime: Long = 0L): List<CoinOperation>
}
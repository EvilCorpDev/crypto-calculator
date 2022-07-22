package com.androidghost77.cryptocalculator.services

import com.androidghost77.cryptocalculator.models.CoinDto
import com.androidghost77.cryptocalculator.models.CoinPriceDto
import com.androidghost77.cryptocalculator.models.binance.BinanceAsset
import com.androidghost77.cryptocalculator.repos.entities.Coin
import com.androidghost77.cryptocalculator.repos.entities.User

interface CoinsService {

    fun getCoins(): List<CoinDto>
    fun getCoinSymbols(): Map<String, Coin>
    fun getCoinsAveragePrice(user: User): List<CoinPriceDto>
    fun addCoin(binanceAsset: BinanceAsset): Coin
    fun getSymbolFromPair(pair: String): String
}
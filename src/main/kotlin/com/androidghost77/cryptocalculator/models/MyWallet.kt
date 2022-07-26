package com.androidghost77.cryptocalculator.models

import java.math.BigDecimal

data class MyWallet(
    val totalValue: BigDecimal,
    val numberOfCoins: Int,
    val coins: List<CoinPriceDto>,
)

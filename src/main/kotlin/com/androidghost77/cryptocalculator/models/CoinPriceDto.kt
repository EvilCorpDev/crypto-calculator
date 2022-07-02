package com.androidghost77.cryptocalculator.models

import java.math.BigDecimal

data class CoinPriceDto(
    val coin: CoinDto,
    val amount: BigDecimal,
    val averagePrice: BigDecimal,
)
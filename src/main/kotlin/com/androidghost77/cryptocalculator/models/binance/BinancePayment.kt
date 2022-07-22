package com.androidghost77.cryptocalculator.models.binance

import java.math.BigDecimal

data class BinancePayment(
    val orderNo: String,
    val sourceAmount: BigDecimal,
    val fiatCurrency: String,
    val obtainAmount: BigDecimal,
    val cryptoCurrency: String,
    val totalFee: String,
    val price: BigDecimal,
    val status: String,
    val createTime: Long,
    val updateTime: Long
)

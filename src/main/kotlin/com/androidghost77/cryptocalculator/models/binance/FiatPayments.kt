package com.androidghost77.cryptocalculator.models.binance

data class FiatPayments(
    val code: String,
    val message: String,
    val data: List<BinancePayment> = emptyList(),
    val total: Int,
    val success: Boolean
)

package com.androidghost77.cryptocalculator.models.binance

import java.math.BigDecimal

data class BinanceTrade(
    val symbol: String,
    val id: Long,
    val orderId: Long,
    val orderListId: Long,
    val price: BigDecimal,
    val qty: BigDecimal,
    val quoteQty: BigDecimal,
    val commission: BigDecimal,
    val commissionAsset: String,
    val time: Long,
    val isBuyer: Boolean,
    val isMaker: Boolean,
    val isBestMatch: Boolean
)
package com.androidghost77.cryptocalculator.models.binance

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.math.BigDecimal

@JsonIgnoreProperties(ignoreUnknown = true)
data class BinanceAccount(
    val makerCommission: Long,
    val takerCommission: Long,
    val buyerCommission: Long,
    val sellerCommission: Long,
    val canTrade: Boolean,
    val canWithdraw: Boolean,
    val canDeposit: Boolean,
    val updateTime: Long,
    val accountType: String,
    val balances: List<AssetBalance>,
)

data class AssetBalance(
    val asset: String,
    val free: BigDecimal,
    val locked: BigDecimal,
)

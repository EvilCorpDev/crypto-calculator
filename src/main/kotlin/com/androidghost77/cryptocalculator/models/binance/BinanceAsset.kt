package com.androidghost77.cryptocalculator.models.binance

data class BinanceAsset(
    val assetName: String,
    val assetFullName: String,
    val isBorrowable: Boolean,
    val isMortgageable: Boolean,
    val userMinBorrow: Long,
    val userMinRepay: Long,
)

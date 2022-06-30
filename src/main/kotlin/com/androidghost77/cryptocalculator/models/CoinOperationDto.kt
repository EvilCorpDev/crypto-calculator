package com.androidghost77.cryptocalculator.models

import java.math.BigDecimal
import java.time.ZonedDateTime

data class CoinOperationDto(
    val name: String,
    val amount: BigDecimal,
    val price: BigDecimal,
    val date: ZonedDateTime
)

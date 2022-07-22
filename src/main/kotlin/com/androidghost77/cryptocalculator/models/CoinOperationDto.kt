package com.androidghost77.cryptocalculator.models

import com.androidghost77.cryptocalculator.enums.OperationType
import java.math.BigDecimal
import java.time.ZonedDateTime

data class CoinOperationDto(
    val name: String,
    val symbol: String,
    val amount: BigDecimal,
    val price: BigDecimal,
    val date: ZonedDateTime,
    val type: OperationType,
)

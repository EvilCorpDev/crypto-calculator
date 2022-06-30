package com.androidghost77.cryptocalculator.repos.entities

import com.androidghost77.cryptocalculator.enums.OperationType
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.time.ZonedDateTime

@Document
data class CoinOperation(
    @field:Id val id: String,
    val name: String,
    val type: OperationType,
    val amount: BigDecimal,
    val price: BigDecimal,
    val date: ZonedDateTime,
    val user: User,
)
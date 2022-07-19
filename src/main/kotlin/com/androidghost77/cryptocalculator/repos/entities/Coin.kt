package com.androidghost77.cryptocalculator.repos.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Coin(
    @field:Id val id: String,
    val name: String,
    val symbol: String,
    val logo: String,
)

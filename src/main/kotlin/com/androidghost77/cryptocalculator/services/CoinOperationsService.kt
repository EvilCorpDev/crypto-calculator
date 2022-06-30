package com.androidghost77.cryptocalculator.services

import com.androidghost77.cryptocalculator.models.CoinOperationDto
import com.androidghost77.cryptocalculator.repos.entities.User

interface CoinOperationsService {
    fun buyCoin(coinOperationDto: CoinOperationDto, user: User)
    fun sellCoin(coinOperationDto: CoinOperationDto, user: User)
}
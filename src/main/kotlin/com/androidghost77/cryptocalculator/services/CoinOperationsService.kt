package com.androidghost77.cryptocalculator.services

import com.androidghost77.cryptocalculator.enums.OperationType
import com.androidghost77.cryptocalculator.models.CoinOperationDto
import com.androidghost77.cryptocalculator.repos.entities.CoinOperation
import com.androidghost77.cryptocalculator.repos.entities.User

interface CoinOperationsService {
    fun buyCoin(coinOperationDto: CoinOperationDto, user: User)
    fun sellCoin(coinOperationDto: CoinOperationDto, user: User)
    fun saveCoinOperation(coinOperation: CoinOperation)
    fun getLatestOperationTime(user: User): Long
    fun getLatestOperationTime(user: User, operationType: OperationType): Long
    fun loadLatestTransactions(user: User)
    fun getCoinOrderHistory(symbol: String): List<CoinOperationDto>
}

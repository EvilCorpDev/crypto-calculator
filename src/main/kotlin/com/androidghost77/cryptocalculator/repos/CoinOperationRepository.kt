package com.androidghost77.cryptocalculator.repos

import com.androidghost77.cryptocalculator.enums.OperationType
import com.androidghost77.cryptocalculator.repos.entities.CoinOperation
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface CoinOperationRepository : MongoRepository<CoinOperation, String> {
    fun findByCoinSymbol(symbol: String): List<CoinOperation>
    fun findFirstByUserIdOrderByDate(userId: String): Optional<CoinOperation>
    fun findFirstByUserIdAndTypeOrderByDate(userId: String, type: OperationType): Optional<CoinOperation>
}
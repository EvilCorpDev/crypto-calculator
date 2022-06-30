package com.androidghost77.cryptocalculator.repos

import com.androidghost77.cryptocalculator.repos.entities.CoinOperation
import org.springframework.data.mongodb.repository.MongoRepository

interface CoinOperationRepository : MongoRepository<CoinOperation, String> {

    fun findByName(name: String): CoinOperation
}
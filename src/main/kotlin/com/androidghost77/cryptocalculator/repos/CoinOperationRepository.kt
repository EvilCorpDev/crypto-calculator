package com.androidghost77.cryptocalculator.repos

import com.androidghost77.cryptocalculator.repos.entities.CoinOperation
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import java.util.Optional

interface CoinOperationRepository : MongoRepository<CoinOperation, String> {
    fun findByName(name: String): CoinOperation

    @Query(value = "{ user.id : ?0 }", sort = "{ date: -1 }")
    fun findFirstByUserId(userId: String): Optional<CoinOperation>
}
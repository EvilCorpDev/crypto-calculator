package com.androidghost77.cryptocalculator.repos

import com.androidghost77.cryptocalculator.repos.entities.Coin
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface CoinRepository : MongoRepository<Coin, String> {
    fun findByName(name: String): Optional<Coin>
    fun findBySymbol(symbol: String): Optional<Coin>
}

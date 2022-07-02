package com.androidghost77.cryptocalculator.services.impl

import com.androidghost77.cryptocalculator.models.CoinDto
import com.androidghost77.cryptocalculator.models.CoinPriceDto
import com.androidghost77.cryptocalculator.repos.CoinOperationRepository
import com.androidghost77.cryptocalculator.repos.entities.CoinOperation
import com.androidghost77.cryptocalculator.repos.entities.User
import com.androidghost77.cryptocalculator.services.CoinsService
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class MongoCoinsService(
    private val coinOperationRepo: CoinOperationRepository,
) : CoinsService {
    override fun getCoins(): List<CoinDto> {
        TODO("Not yet implemented")
    }

    override fun getCoinsAveragePrice(user: User): List<CoinPriceDto> {
        val operations: List<CoinOperation> = coinOperationRepo.findAll()
        val coinPrices = mutableMapOf<String, Pair<BigDecimal, BigDecimal>>()
        operations.forEach { item ->
            coinPrices[item.name] = Pair(
                (coinPrices[item.name]?.first ?: BigDecimal.ZERO) + item.price * item.amount,
                (coinPrices[item.name]?.second ?: BigDecimal.ZERO) + item.amount
            )
        }
        return coinPrices.map { it.key to Pair(it.value.first / it.value.second, it.value.second) }
            .map { CoinPriceDto(CoinDto(it.first, ""), it.second.second, it.second.first) }
    }
}
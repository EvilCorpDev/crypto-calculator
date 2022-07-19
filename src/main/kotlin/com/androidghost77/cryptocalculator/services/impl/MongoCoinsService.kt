package com.androidghost77.cryptocalculator.services.impl

import com.androidghost77.cryptocalculator.enums.OperationType
import com.androidghost77.cryptocalculator.exceptions.CoinAlreadyExistsException
import com.androidghost77.cryptocalculator.mappers.CoinMapper
import com.androidghost77.cryptocalculator.models.CoinDto
import com.androidghost77.cryptocalculator.models.CoinPriceDto
import com.androidghost77.cryptocalculator.models.binance.BinanceAsset
import com.androidghost77.cryptocalculator.repos.CoinOperationRepository
import com.androidghost77.cryptocalculator.repos.CoinRepository
import com.androidghost77.cryptocalculator.repos.entities.Coin
import com.androidghost77.cryptocalculator.repos.entities.CoinOperation
import com.androidghost77.cryptocalculator.repos.entities.User
import com.androidghost77.cryptocalculator.services.CoinsService
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*

@Service
class MongoCoinsService(
    private val coinOperationRepo: CoinOperationRepository,
    private val coinRepository: CoinRepository,
    private val coinMapper: CoinMapper,
) : CoinsService {
    override fun getCoins(): List<CoinDto> =
        coinRepository.findAll()
            .map { coinMapper.coinToCoinDto(it) }

    override fun getCoinSymbols(): Map<String, CoinDto> =
        coinRepository.findAll()
            .map { coinMapper.coinToCoinDto(it) }
            .associateBy { it.symbol }

    override fun getCoinsAveragePrice(user: User): List<CoinPriceDto> {
        val operations: List<CoinOperation> = coinOperationRepo.findAll()
        val coinPrices = mutableMapOf<String, Pair<BigDecimal, BigDecimal>>()
        operations.forEach { item ->
            coinPrices[item.name] = Pair(
                (coinPrices[item.name]?.first ?: BigDecimal.ZERO) + getValueByOperation(
                    item.type,
                    item.price * item.amount
                ),
                (coinPrices[item.name]?.second ?: BigDecimal.ZERO) + getValueByOperation(item.type, item.amount)
            )
        }
        return coinPrices.map { it.key to Pair(it.value.first / it.value.second, it.value.second) }
            .map { CoinPriceDto(CoinDto(it.first, "", ""), it.second.second, it.second.first) }
    }

    override fun addCoin(binanceAsset: BinanceAsset): CoinDto {
        if (coinRepository.findByName(binanceAsset.assetFullName).isPresent) {
            throw CoinAlreadyExistsException("Coin ${binanceAsset.assetFullName} already exists")
        }

        val coin = Coin(
            id = UUID.randomUUID().toString(),
            name = binanceAsset.assetFullName,
            symbol = binanceAsset.assetName,
            logo = "",
        )

        return coinMapper.coinToCoinDto(coinRepository.save(coin))
    }

    private fun getValueByOperation(type: OperationType, value: BigDecimal) =
        when (type) {
            OperationType.BUY -> value
            OperationType.SELL -> value * BigDecimal.valueOf(-1L)
        }
}
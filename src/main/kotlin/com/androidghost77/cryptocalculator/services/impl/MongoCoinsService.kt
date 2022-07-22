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

    override fun getCoinSymbols(): Map<String, Coin> =
        coinRepository.findAll()
            .associateBy { it.symbol }

    override fun getCoinsAveragePrice(user: User): List<CoinPriceDto> {
        val operations: List<CoinOperation> = coinOperationRepo.findAll()
        val coinPrices = mutableMapOf<Coin, Pair<BigDecimal, BigDecimal>>()
        operations.forEach { item ->
            coinPrices[item.coin] = Pair(
                (coinPrices[item.coin]?.first ?: BigDecimal.ZERO) + getValueByOperation(
                    item.type,
                    item.price * item.amount
                ),
                (coinPrices[item.coin]?.second ?: BigDecimal.ZERO) + getValueByOperation(item.type, item.amount)
            )
        }
        return coinPrices.map { it.key to Pair(it.value.first / it.value.second, it.value.second) }
            .map { CoinPriceDto(coinMapper.coinToCoinDto(it.first), it.second.second, it.second.first) }
    }

    override fun addCoin(binanceAsset: BinanceAsset): Coin {
        if (coinRepository.findByName(binanceAsset.assetFullName).isPresent) {
            throw CoinAlreadyExistsException("Coin ${binanceAsset.assetFullName} already exists")
        }

        val coin = Coin(
            id = UUID.randomUUID().toString(),
            name = binanceAsset.assetFullName,
            symbol = binanceAsset.assetName,
            logo = "",
        )

        return coinRepository.save(coin)
    }

    override fun getSymbolFromPair(pair: String): String {
        val stableCoin = STABLE_COINS_LIST.first { pair.contains(it) }
        return pair.substring(0, pair.length - stableCoin.length)
    }

    private fun getValueByOperation(type: OperationType, value: BigDecimal) =
        when (type) {
            OperationType.BUY, OperationType.BUY_BY_FIAT -> value
            OperationType.SELL -> value * BigDecimal.valueOf(-1L)
        }

    companion object {
        val STABLE_COINS_LIST = listOf("USDT", "USDC", "BUSD")
    }
}
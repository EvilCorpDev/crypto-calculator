package com.androidghost77.cryptocalculator.services.impl

import com.androidghost77.cryptocalculator.enums.OperationType
import com.androidghost77.cryptocalculator.mappers.CoinOperationMapper
import com.androidghost77.cryptocalculator.models.CoinOperationDto
import com.androidghost77.cryptocalculator.repos.CoinOperationRepository
import com.androidghost77.cryptocalculator.repos.entities.Coin
import com.androidghost77.cryptocalculator.repos.entities.CoinOperation
import com.androidghost77.cryptocalculator.repos.entities.User
import com.androidghost77.cryptocalculator.services.BinanceLoader
import com.androidghost77.cryptocalculator.services.CoinOperationsService
import com.androidghost77.cryptocalculator.services.CoinsService
import com.androidghost77.cryptocalculator.services.Logging
import com.androidghost77.cryptocalculator.services.impl.MongoCoinsService.Companion.STABLE_COINS_LIST
import com.binance.connector.client.exceptions.BinanceClientException
import com.binance.connector.client.impl.SpotClientImpl
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

@Service
class MongoCoinOperationsService(
    private val coinOperationsRepo: CoinOperationRepository,
    private val coinOperationsMapper: CoinOperationMapper,
    private val coinsService: CoinsService,
    private val binanceLoader: BinanceLoader,
) : CoinOperationsService {
    override fun addCoinOperation(coinOperationDto: CoinOperationDto, user: User) {
        val coin = coinsService.findCoin(coinOperationDto.symbol)
        val entity: CoinOperation = coinOperationsMapper.toEntity(coinOperationDto, user, coin)
        coinOperationsRepo.save(entity)
    }

    override fun saveCoinOperation(coinOperation: CoinOperation) {
        coinOperationsRepo.save(coinOperation)
    }

    override fun getLatestOperationTime(user: User): Long =
        coinOperationsRepo.findFirstByUserIdOrderByDateDesc(user.id)
            .map { it.date.toInstant().toEpochMilli() }
            .orElse(0L)

    override fun getLatestOperationTime(user: User, operationType: OperationType): Long {
        val defaultDate = ZonedDateTime.of(ZonedDateTime.now().year, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC"))
        return coinOperationsRepo.findFirstByUserIdAndTypeOrderByDateDesc(user.id, operationType)
            .map { it.date.toInstant().toEpochMilli() }
            .orElse(defaultDate.toInstant().toEpochMilli())
    }

    override fun loadLatestTransactions(user: User) {
        val client = SpotClientImpl(user.binanceToken, user.secretBinanceKey)
        val coins: List<Coin> = binanceLoader.loadAccountCoins(client)
        val latestOperationTime = getLatestOperationTime(user)

        coins.forEach { coin ->
            STABLE_COINS_LIST.forEach { stableCoin ->
                if (!STABLE_COINS_LIST.contains(coin.symbol)) {
                    try {
                        binanceLoader.loadOperationsHistory(
                            client,
                            user,
                            "${coin.symbol}$stableCoin",
                            latestOperationTime
                        ).forEach { coinOperation ->
                            saveCoinOperation(coinOperation)
                        }
                    } catch (exc: BinanceClientException) {
                        logger().error { "Can't retrieve data from binance for pair: ${coin.symbol}$stableCoin, ${exc.errMsg}" }
                    }
                }
            }
        }

        val latestOperationTimeForCryptoBuy = getLatestOperationTime(user, OperationType.BUY_BY_FIAT)
        binanceLoader.loadBuyCryptoHistory(
            client,
            user,
            Instant.ofEpochMilli(latestOperationTimeForCryptoBuy)
                .atZone(ZoneId.of("UTC"))
        )
            .filter { coins.contains(it.coin) }
            .forEach { saveCoinOperation(it) }
    }

    override fun getCoinOrderHistory(symbol: String): List<CoinOperationDto> =
        coinOperationsRepo.findByCoinSymbol(symbol)
            .map { coinOperationsMapper.toDto(it) }
            .sortedByDescending { it.date }

    companion object : Logging
}

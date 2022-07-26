package com.androidghost77.cryptocalculator.services.impl

import com.androidghost77.cryptocalculator.enums.OperationType
import com.androidghost77.cryptocalculator.models.binance.*
import com.androidghost77.cryptocalculator.repos.entities.Coin
import com.androidghost77.cryptocalculator.repos.entities.CoinOperation
import com.androidghost77.cryptocalculator.repos.entities.User
import com.androidghost77.cryptocalculator.services.BinanceLoader
import com.androidghost77.cryptocalculator.services.CoinsService
import com.androidghost77.cryptocalculator.services.Logging
import com.androidghost77.cryptocalculator.services.impl.MongoCoinsService.Companion.STABLE_COINS_LIST
import com.binance.connector.client.SpotClient
import com.binance.connector.client.exceptions.BinanceClientException
import com.binance.connector.client.impl.SpotClientImpl
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.*

@Service
class BinanceHttpLoader(
    private val objectMapper: ObjectMapper,
    private val coinsService: CoinsService,
) : BinanceLoader {
    override fun loadAssetAndSaveCoin(client: SpotClient, symbol: String): Coin {
        return try {
            val asset: BinanceAsset =
                objectMapper.readValue(client.createMargin().asset(linkedMapOf("asset" to symbol)))
            return coinsService.addCoin(asset)
        } catch (exc: BinanceClientException) {
            // Add retry here
            logger().info { "Cant find asset $symbol. Binance response: ${exc.errMsg}" }
            Coin("", symbol, symbol, "")
        }
    }

    override fun loadAccountCoins(client: SpotClient): List<Coin> {
        val account: BinanceAccount = objectMapper.readValue(client.createTrade().account(linkedMapOf()))
        val coinSymbols: Map<String, Coin> = coinsService.getCoinSymbols()

        return account.balances
            .filter { it.free > BigDecimal.ZERO || it.locked > BigDecimal.ZERO }
            .map {
                Coin(
                    id = UUID.randomUUID().toString(),
                    name = coinSymbols[it.asset]?.name ?: loadAssetAndSaveCoin(client, it.asset).name,
                    symbol = it.asset,
                    logo = ""
                )
            }
    }

    override fun loadOperationsHistory(
        client: SpotClient,
        user: User,
        symbol: String,
        startTime: Long,
    ): List<CoinOperation> {
        val myTrades = client.createTrade().myTrades(linkedMapOf("symbol" to symbol, "startTime" to startTime))

        val trades: List<BinanceTrade> = objectMapper.readValue(myTrades)
        val coinSymbols: Map<String, Coin> = coinsService.getCoinSymbols()
        val assetSymbol = coinsService.getSymbolFromPair(symbol)
        val coin: Coin = coinSymbols[assetSymbol] ?: loadAssetAndSaveCoin(
            client,
            assetSymbol
        )

        return trades.map {
            CoinOperation(
                id = UUID.randomUUID().toString(),
                coin = coin,
                type = if (it.isBuyer) OperationType.BUY else OperationType.SELL,
                amount = it.qty,
                price = it.price,
                date = Instant.ofEpochMilli(it.time).atZone(ZoneId.of("UTC")),
                user = user
            )
        }

    }

    override fun loadBuyCryptoHistory(
        client: SpotClient,
        user: User,
        startTime: ZonedDateTime
    ): List<CoinOperation> {
        val between: Long = ChronoUnit.MONTHS.between(startTime, ZonedDateTime.now())
        var startDateTime = startTime
        val result = mutableListOf<BinancePayment>()
        for (i in 0..between step 3) {
            val payments: FiatPayments = objectMapper.readValue(
                client.createFiat().payments(
                    linkedMapOf(
                        "transactionType" to "0",
                        "beginTime" to startDateTime.toInstant().toEpochMilli(),
                        "endTime" to getEndTime(startDateTime).toInstant().toEpochMilli()
                    )
                )
            )
            result.addAll(payments.data)
            startDateTime = getEndTime(startTime).plusDays(1)
        }
        val coinSymbols: Map<String, Coin> = coinsService.getCoinSymbols()
        return convertToCoinOperation(result, coinSymbols, client, user)
    }

    /***
     * klines has next structure:
     *  [[
     *     1499040000000,      // Open time
     *     "0.01634790",       // Open
     *     "0.80000000",       // High
     *     "0.01575800",       // Low
     *     "0.01577100",       // Close
     *     "148976.11427815",  // Volume
     *     1499644799999,      // Close time
     *     "2434.19055334",    // Quote asset volume
     *     308,                // Number of trades
     *     "1756.87402397",    // Taker buy base asset volume
     *     "28.46694368",      // Taker buy quote asset volume
     *     "17928899.62484339" // Ignore.
     *  ]]
     * So we get highest(2 index) and lowest (third index) price and get average of those
     */
    override fun getAssetPrice(client: SpotClient, symbol: String, date: ZonedDateTime): BigDecimal {
        val klines = client.createMarket().klines(
            linkedMapOf(
                "symbol" to symbol,
                "interval" to "1d",
                "startTime" to date.toInstant().toEpochMilli(),
                "endTime" to date.plusDays(1).toInstant().toEpochMilli(),
            )
        )
        val result: Array<Array<BigDecimal>> = objectMapper.readValue(klines)
        return if (result.isNotEmpty()) (result[0][2] + result[0][3]) / BigDecimal(2) else BigDecimal(-1)
    }

    private fun getEndTime(startDateTime: ZonedDateTime): ZonedDateTime {
        val endDate = startDateTime.plusMonths(3).minusDays(1)
        return if (endDate.isAfter(ZonedDateTime.now())) ZonedDateTime.now() else endDate
    }

    private fun convertToCoinOperation(
        result: MutableList<BinancePayment>,
        coinSymbols: Map<String, Coin>,
        client: SpotClient,
        user: User
    ): List<CoinOperation> = result
        .filter { it.status == "Completed" }
        .filter { !STABLE_COINS_LIST.contains(it.cryptoCurrency) }
        .map {
            val coin =
                coinSymbols[it.cryptoCurrency] ?: loadAssetAndSaveCoin(
                    client,
                    it.cryptoCurrency
                )
            val date = Instant.ofEpochMilli(it.updateTime).atZone(ZoneId.of("UTC"))
            CoinOperation(
                id = UUID.randomUUID().toString(),
                coin = coin,
                type = OperationType.BUY_BY_FIAT,
                amount = it.obtainAmount,
                price = getAssetPrice(client, "${coin.symbol}USDT", date),
                date = date,
                user = user,
            )
        }


    companion object : Logging
}

fun main() {
    val client = SpotClientImpl(
        "jOGBu13eZWKXKxYr8iwobnvezs9ofH8USX8CMwkQzn8Qvp0Tdb1Z7S6Ss4rOyJ0P",
        "flOBRzMqU08vYr98yFoXNdgrXC0nylOmHrGD8HWDixgjv9ONAN0N7PlT7jOvdwLe"
    )
    val year = ZonedDateTime.of(2022, 3, 1, 1, 1, 1, 1, ZoneId.of("UTC"))
    val threeMonth = year.plusMonths(2).plusDays(29)
    println(
        client.createFiat().payments(
            linkedMapOf(
                "currency" to "USD",
                "transactionType" to "0",
                "beginTime" to year.toInstant().toEpochMilli(),
                "endTime" to threeMonth.toInstant().toEpochMilli()
            )
        )
    )

}
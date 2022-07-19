package com.androidghost77.cryptocalculator.services.impl

import com.androidghost77.cryptocalculator.enums.OperationType
import com.androidghost77.cryptocalculator.models.CoinDto
import com.androidghost77.cryptocalculator.models.binance.BinanceAccount
import com.androidghost77.cryptocalculator.models.binance.BinanceAsset
import com.androidghost77.cryptocalculator.models.binance.BinanceTrade
import com.androidghost77.cryptocalculator.repos.entities.Coin
import com.androidghost77.cryptocalculator.repos.entities.CoinOperation
import com.androidghost77.cryptocalculator.repos.entities.User
import com.androidghost77.cryptocalculator.services.BinanceLoader
import com.androidghost77.cryptocalculator.services.CoinsService
import com.binance.connector.client.SpotClient
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.Instant
import java.time.ZoneId
import java.util.*

@Service
class BinanceHttpLoader(
    private val objectMapper: ObjectMapper,
    private val coinsService: CoinsService,
) : BinanceLoader {
    override fun findAssetFullNameAndSaveCoin(client: SpotClient, symbol: String): String {
        val asset: BinanceAsset = objectMapper.readValue(client.createMargin().asset(linkedMapOf("asset" to symbol)))
        coinsService.addCoin(asset)

        return asset.assetFullName
    }

    override fun loadAccountCoins(client: SpotClient): List<Coin> {
        val account: BinanceAccount = objectMapper.readValue(client.createTrade().account(linkedMapOf()))
        val coinSymbols: Map<String, CoinDto> = coinsService.getCoinSymbols()

        return account.balances
            .filter { it.free > BigDecimal.ZERO || it.locked > BigDecimal.ZERO }
            .map {
                Coin(
                    id = UUID.randomUUID().toString(),
                    name = coinSymbols[it.asset]?.name ?: findAssetFullNameAndSaveCoin(client, it.asset),
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
        val coinSymbols: Map<String, CoinDto> = coinsService.getCoinSymbols()

        return trades.map {
            CoinOperation(
                id = UUID.randomUUID().toString(),
                name = coinSymbols[it.symbol]?.name ?: findAssetFullNameAndSaveCoin(client, it.symbol),
                type = if (it.isBuyer) OperationType.BUY else OperationType.SELL,
                amount = it.qty,
                price = it.price,
                date = Instant.ofEpochMilli(it.time).atZone(ZoneId.of("UTC")),
                user = user
            )
        }

    }
}
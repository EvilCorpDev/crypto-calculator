package com.androidghost77.cryptocalculator.jobs

import com.androidghost77.cryptocalculator.security.DbUserDetailsManager
import com.androidghost77.cryptocalculator.services.CoinOperationsService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class RetrieveBinanceTradeHistoryJob(
    private val userDetailsManager: DbUserDetailsManager,
    private val coinOperationsService: CoinOperationsService,
) {

    @Scheduled(cron = "")
    fun updateUserOrderHistory() {
        val allUsers = userDetailsManager.getAllUsers()
        allUsers.forEach { coinOperationsService.loadLatestTransactions(it) }
    }
}
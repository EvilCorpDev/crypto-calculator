package com.androidghost77.cryptocalculator.services.impl

import com.androidghost77.cryptocalculator.enums.OperationType
import com.androidghost77.cryptocalculator.mappers.CoinOperationMapper
import com.androidghost77.cryptocalculator.models.CoinOperationDto
import com.androidghost77.cryptocalculator.repos.CoinOperationRepository
import com.androidghost77.cryptocalculator.repos.entities.CoinOperation
import com.androidghost77.cryptocalculator.repos.entities.User
import com.androidghost77.cryptocalculator.services.CoinOperationsService
import org.springframework.stereotype.Service

@Service
class MongoCoinOperationsService(
    val coinOperationsRepo: CoinOperationRepository,
    val coinOperationsMapper: CoinOperationMapper,
) : CoinOperationsService {
    override fun buyCoin(coinOperationDto: CoinOperationDto, user: User) {
        val entity: CoinOperation = coinOperationsMapper.toEntity(coinOperationDto, user, OperationType.BUY)
        coinOperationsRepo.save(entity)
    }

    override fun sellCoin(coinOperationDto: CoinOperationDto, user: User) {
        val entity: CoinOperation = coinOperationsMapper.toEntity(coinOperationDto, user, OperationType.SELL)
        coinOperationsRepo.save(entity)
    }
}

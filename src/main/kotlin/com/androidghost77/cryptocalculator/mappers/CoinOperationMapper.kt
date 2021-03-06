package com.androidghost77.cryptocalculator.mappers

import com.androidghost77.cryptocalculator.models.CoinOperationDto
import com.androidghost77.cryptocalculator.repos.entities.Coin
import com.androidghost77.cryptocalculator.repos.entities.CoinOperation
import com.androidghost77.cryptocalculator.repos.entities.User
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface CoinOperationMapper {

    @Mapping(expression = "java(java.util.UUID.randomUUID().toString())", target = "id")
    fun toEntity(source: CoinOperationDto, user: User, coin: Coin): CoinOperation

    @Mapping(target = "name", source = "source.coin.name")
    @Mapping(target = "symbol", source = "source.coin.symbol")
    fun toDto(source: CoinOperation): CoinOperationDto
}
package com.androidghost77.cryptocalculator.mappers

import com.androidghost77.cryptocalculator.models.CoinDto
import com.androidghost77.cryptocalculator.repos.entities.Coin
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface CoinMapper {

    fun coinToCoinDto(coin: Coin): CoinDto
}
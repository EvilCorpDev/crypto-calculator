package com.androidghost77.cryptocalculator.configs

import com.androidghost77.cryptocalculator.repos.converters.ZonedDateTimeReadConverter
import com.androidghost77.cryptocalculator.repos.converters.ZonedDateTimeWriteConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.convert.MongoCustomConversions

@Configuration
class MongoConfiguration {

    @Bean
    fun mongoCustomConversions(): MongoCustomConversions =
        MongoCustomConversions(listOf(ZonedDateTimeReadConverter(), ZonedDateTimeWriteConverter()))
}
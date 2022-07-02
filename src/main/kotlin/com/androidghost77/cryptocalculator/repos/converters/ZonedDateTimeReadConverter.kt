package com.androidghost77.cryptocalculator.repos.converters

import org.springframework.core.convert.converter.Converter
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*

class ZonedDateTimeReadConverter : Converter<Date, ZonedDateTime> {
    override fun convert(source: Date): ZonedDateTime = source.toInstant().atZone(ZoneOffset.UTC)
}
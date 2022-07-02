package com.androidghost77.cryptocalculator.repos.converters

import org.springframework.core.convert.converter.Converter
import java.time.ZonedDateTime
import java.util.*

class ZonedDateTimeWriteConverter : Converter<ZonedDateTime, Date> {
    override fun convert(source: ZonedDateTime): Date = Date.from(source.toInstant())
}
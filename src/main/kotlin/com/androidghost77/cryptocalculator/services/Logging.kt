package com.androidghost77.cryptocalculator.services

import mu.KLogger
import mu.KotlinLogging

interface Logging {
    fun <T : Logging> T.logger(): KLogger = KotlinLogging.logger {}
}
package com.ktHat.Utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.system.measureNanoTime

fun getTime(): Double =
    System.currentTimeMillis() / 1000.00

fun runOnIO(action: () -> Unit): CoroutineScope {
    val coroutineScope = CoroutineScope(EmptyCoroutineContext)
    coroutineScope.launch(Dispatchers.IO) {
        action()
    }
    return coroutineScope
}
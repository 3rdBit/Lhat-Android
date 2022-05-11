package com.ktHat.Utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.coroutines.EmptyCoroutineContext

fun getTime(pattern: String = "yyyy-MM-dd HH:mm:ss"): String = LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern))

fun runOnIO(action: () -> Unit) {
    val coroutineScope = CoroutineScope(EmptyCoroutineContext)
    coroutineScope.launch(Dispatchers.IO) {
        action()
    }
}
package com.ktHat.Utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

fun getTime(): Double =
    System.currentTimeMillis() / 1000.00

fun runOnIO(action: () -> Unit): CoroutineScope {
    val coroutineScope = CoroutineScope(EmptyCoroutineContext)
    coroutineScope.launch(Dispatchers.IO) {
        action()
    }
    return coroutineScope
}

fun runOnMain(action: () -> Unit): CoroutineScope {
    val coroutineScope = CoroutineScope(EmptyCoroutineContext)
    coroutineScope.launch(Dispatchers.Main) {
        action()
    }
    return coroutineScope
}

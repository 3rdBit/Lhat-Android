package com.ktHat.Utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

fun getTime(): Double = System.currentTimeMillis() / 1000.00

fun runOnIO(
    coroutineScope: CoroutineScope = CoroutineScope(EmptyCoroutineContext), action: () -> Unit
): CoroutineScope {
    coroutineScope.launch(Dispatchers.IO) {
        action()
    }
    return coroutineScope
}

fun runOnMain(
    coroutineScope: CoroutineScope = CoroutineScope(EmptyCoroutineContext), action: () -> Unit
): CoroutineScope {
    coroutineScope.launch(Dispatchers.Main) {
        action()
    }
    return coroutineScope
}

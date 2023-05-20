package com.third.lhat.dependency.kthat.base.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

suspend fun <T> runOnIO(
    coroutineScope: CoroutineScope = CoroutineScope(EmptyCoroutineContext), action: () -> T
): T {
    return withContext(coroutineScope.coroutineContext + Dispatchers.IO) {
        action()
    }
}

suspend fun <T> runOnMain(
    coroutineScope: CoroutineScope = CoroutineScope(EmptyCoroutineContext), action: () -> T
): T {
    return withContext(coroutineScope.coroutineContext + Dispatchers.Main) {
        action()
    }
}
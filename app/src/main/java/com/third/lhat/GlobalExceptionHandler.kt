package com.third.lhat

import androidx.lifecycle.ViewModelProvider

class GlobalExceptionHandler : Thread.UncaughtExceptionHandler{
    override fun uncaughtException(p0: Thread, p1: Throwable) : Nothing {
        Objects.composeActivityInstance?.let {
            val viewModel: ViewModel = ViewModelProvider(it)[ViewModel::class.java]
            viewModel.connection?.close()
        }
        throw p1
    }
}

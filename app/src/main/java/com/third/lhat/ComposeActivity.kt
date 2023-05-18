package com.third.lhat

import Main
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider

class ComposeActivity : ComponentActivity() {
    companion object : Startable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent(content = Main())
    }

    override fun onDestroy() {
        ViewModelProvider(this)[ViewModel::class.java].connection?.close()
        super.onDestroy()
    }

}


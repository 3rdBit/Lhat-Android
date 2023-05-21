package com.third.lhat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.third.lhat.compose.unit.Main

class ComposeActivity : ComponentActivity() {
    companion object : Startable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Database.applicationContext = applicationContext
        setContent(content = Main())
    }

    override fun onDestroy() {
        ViewModelProvider(this)[ViewModel::class.java].connection?.close()
        super.onDestroy()
    }

}


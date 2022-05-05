package com.third.lhat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ktHat.Messages.Message

class ViewModel: ViewModel() {
    var selectedBar by mutableStateOf(0)
    var bottomTabVisibility by mutableStateOf(true)
    var editing by mutableStateOf(false)
    val selectedMessage = mutableStateListOf<Message>()
    var username by mutableStateOf("")
}
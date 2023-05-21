package com.third.lhat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.third.lhat.dependency.kthat.base.messages.Message
import com.third.lhat.dependency.kthat.base.models.Connection
import com.third.lhat.dependency.kthat.base.models.Chat

class ViewModel: ViewModel() {
    var selectedBar by mutableStateOf(0)
    var bottomTabVisibility by mutableStateOf(true)
    var editing by mutableStateOf(false)
    val editingSelectedMessage = mutableStateListOf<Message>()
    var username by mutableStateOf("")
    var connection: Connection? = null
    var currentChat: Chat by mutableStateOf(Chat.emptyChat)
    var groupName: String? = null
    var onlineList by mutableStateOf(listOf<String>())
    val readList = mutableStateListOf<Message>()
}
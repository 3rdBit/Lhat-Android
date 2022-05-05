package com.third.lhat.Base.Models

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import com.ktHat.Messages.Message
import com.third.lhat.ViewModel

data class Chat private constructor(val message: Message) {
    val messageList = mutableStateListOf(message)
    val lastMessage: Message
        get() = messageList.last()

    companion object {
        val chatMap = mutableStateMapOf<String, Chat>()
        fun addMessage(message: Message) {
            val viewModel = ViewModel()
            val to = if (message.sender != viewModel.username) {
                message.sender
            } else {
                message.receiver
            }

            if (to in chatMap.keys) {
                chatMap[to]?.run{
                    messageList.add(message)
                }
            } else {
                val chat = Chat(message)
                chatMap[to] = chat
            }
        }

        fun getChatOrNull(username: String): Chat? {
            return chatMap[username]
        }
    }
}
package com.third.lhat.Base.Models

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.ktHat.Messages.Message
import com.third.lhat.ViewModel

var Message.isRead: Boolean
    get() = MessageRead.getRead(this)
    set(boolean) = MessageRead.setRead(this, boolean)


object MessageRead {
    private val readList = mutableListOf<Message>()

    fun setRead(message: Message, boolean: Boolean) {
        if (boolean) {
            if (message !in readList) {
                readList.add(message)
            }
        } else {
            if (message in readList) {
                readList.remove(message)
            }
        }
    }

    fun getRead(message: Message): Boolean {
        return message in readList
    }
}


data class Chat private constructor(val messageList: SnapshotStateList<Message>) {
    constructor(message: Message) : this(mutableStateListOf(message))

    val lastMessage: Message
        get() = messageList.last()

    val unreadMessageNumber: Int
        get() = messageList.count {
            !it.isRead
        }

    fun readAllMessage() {
        this.messageList.forEach {
            it.isRead = true
        }
    }

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
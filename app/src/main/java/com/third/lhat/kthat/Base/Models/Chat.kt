package com.third.lhat.kthat.Base.Models

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.ktHat.Messages.Message
import com.ktHat.Messages.TextMessage
import com.ktHat.Statics.Objects.viewModel
import com.third.lhat.flush
import com.third.lhat.kthat.Base.Messages.EmptyMessage

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

    private val me = viewModel.username

    private val others = lastMessage.run {
        if (sender == viewModel.username) receiver else sender
    }

    val lastMessage: Message
        get() = messageList.last()

    val unreadMessageNumber: Int
        get() = messageList.count {
            !it.isRead
        }

    fun readAllMessage() {
        if (messageList.isEmpty()) {
            return
        }
        messageList.forEach {
            it.isRead = true
        }
        messageList.flush()
    }

    fun newTextMessage(text: String): TextMessage {
        return TextMessage(
            sender = me,
            receiver = others,
            rawMessage = text
        )
    }

    companion object {
//        private val viewModel: ViewModel
//            get() = ViewModelProvider(ComposeActivity()).get(ViewModel::class.java)
        val emptyChat = Chat(EmptyMessage())

        val chatMap = mutableStateMapOf<String, Chat>()
        fun addMessage(message: Message) {
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
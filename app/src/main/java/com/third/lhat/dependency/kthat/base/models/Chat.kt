package com.third.lhat.dependency.kthat.base.models

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.third.lhat.dependency.kthat.base.messages.Message
import com.third.lhat.dependency.kthat.base.messages.MessageType
import com.third.lhat.dependency.kthat.base.messages.TextMessage
import com.third.lhat.Objects.viewModel
import com.third.lhat.flush
import com.third.lhat.dependency.kthat.base.models.MessageRead.getRead
import com.third.lhat.dependency.kthat.base.models.MessageRead.setRead
import com.third.lhat.dependency.kthat.base.messages.EmptyMessage

var Message.isRead: Boolean
    inline get() = this.getRead()
    inline set(boolean) = setRead(boolean)


object MessageRead {
    fun Message.setRead(boolean: Boolean = true) {
        if (boolean) {
            if (this !in viewModel.readList) {
                viewModel.readList.add(this)
            }
        } else {
            if (this in viewModel.readList) {
                viewModel.readList.remove(this)
            }
        }
    }

    fun Message.getRead(): Boolean {
        return this in viewModel.readList
    }
}


data class Chat private constructor(
    val messageList: SnapshotStateList<Message>,
    private val isGroup: Boolean = false
) {
    constructor(message: Message, isGroup: Boolean = false) : this(
        mutableStateListOf(message),
        isGroup
    )

    private val me = viewModel.username

    val others = lastMessage.run {
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
            val target =
                if (message.receiver == viewModel.groupName) message.receiver.also {
                    message.member = message.sender
                    message.sender = message.receiver
                    message.isInGroup = true
                } else if (message.sender == viewModel.username) message.receiver
                else message.sender

            if (target in chatMap.keys) {
                chatMap[target]?.run {
                    messageList.add(message)
                }
                return
            }
            val chat = Chat(message)
            chatMap[target] = chat

        }

        fun setGroup(name: String) {
            if (name in chatMap.keys) {
                return
            }
            val chat = Chat(
                TextMessage(
                    sender = name,
                    receiver = viewModel.username,
                    rawMessage = "",
                    type = MessageType.EMPTY
                ).also { it.setRead() }, isGroup = true
            )
            viewModel.groupName = name
            chatMap[name] = chat
        }

        fun getChatOrNull(username: String): Chat? {
            return chatMap[username]
        }
    }
}
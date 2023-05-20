package com.third.lhat.dependency.kthat.base.messages

import com.third.lhat.dependency.kthat.base.models.UnknownMessage

data class FileRecvMessage(override val json: String
) : Message() {
    companion object : Parse(){
        override fun parse(message: UnknownMessage): TextMessage {
            return TextMessage(
                sender = message.sender,
                receiver = message.receiver,
                time = message.time,
                rawMessage = "[文件]暂不支持的文件消息"
            )
        }
    }
}
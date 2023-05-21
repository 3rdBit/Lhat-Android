package com.third.lhat.dependency.kthat.base.messages

import com.third.lhat.dependency.kthat.base.models.UnknownMessage
import com.third.lhat.Objects
import com.squareup.moshi.adapter

data class DefaultRoomMessage(
    override val rawMessage: String
) : Message() {
    override val type: MessageType = MessageType.DEFAULT_ROOM
    @OptIn(ExperimentalStdlibApi::class)
    override val json: String
        get() {
            val adapter = Objects.moshi.adapter<DefaultRoomMessage>()
            return adapter.toJson(this)
        }
    companion object : Parse() {
        override fun parse(message: UnknownMessage): DefaultRoomMessage {
            return DefaultRoomMessage(rawMessage = message.rawMessage)
        }
    }

}
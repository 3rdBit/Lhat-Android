package com.third.lhat.dependency.kthat.base.messages

data class EmptyMessage(
    override val json: String = "",
    override val type: MessageType? = MessageType.EMPTY
    //override val end: String = ""
) : Message()
package com.ktHat.Messages

import com.ktHat.Models.UnknownMessage
import com.ktHat.Statics.END
import com.ktHat.Statics.Objects
import com.squareup.moshi.adapter

/**
 * 此消息类型为在线列表的原始形态。
 * 不应手动操作此类型。
 */

data class OnlineListMessage(
//    override val sender: User,
//    override val receiver: User,
    /*
        兼容性更改
     */
    override val sender: String,
    override val receiver: String,
    override val type: MessageType = MessageType.USER_MANIFEST,
    override val rawMessage: String,
    override val time: String,
    override val file: String?,
    override val end: String = END
) : Message() {
    constructor(message: UnknownMessage) : this(
        message.sender,
        message.receiver,
        message.type ?: throw NullPointerException(),
        message.rawMessage,
        message.time,
        message.file
    )

    @OptIn(ExperimentalStdlibApi::class)
    override val json: String
        get() {
            val adapter = Objects.moshi.adapter<OnlineListMessage>()
            return adapter.toJson(this)
        }

}
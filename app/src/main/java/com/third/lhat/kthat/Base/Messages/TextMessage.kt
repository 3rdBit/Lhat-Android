package com.ktHat.Messages

import com.ktHat.Models.UnknownMessage
import com.ktHat.Statics.END
import com.ktHat.Statics.Objects.moshi
import com.ktHat.Utils.getTime
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.adapter

/**
 * 参数列表：
 * sender: String - 消息的发送者。
 * receiver: String - 消息的接收者。
 * rawMessage: String - 消息的文本内容。
 * TODO: file: String - 消息附带的文件。
 */


@JsonClass(generateAdapter = true)
data class TextMessage private constructor(
//    @Json(name = "by") override val sender: User,
//    @Json(name = "to") override val receiver: User,
    /*
        兼容性更改
     */
    @Json(name = "by") override val sender: String,
    @Json(name = "to") override val receiver: String,
    override val type: MessageType = MessageType.TEXT_MESSAGE_ARTICLE,
    override val time: String = getTime(),
    @Json(name = "message") override val rawMessage: String,
    override val file: String? = null,
    override val end: String = END

) : Message() {
    constructor(message: UnknownMessage) : this(
        message.sender,
        message.receiver,
        message.type ?: throw NullPointerException(),
        message.time,
        message.rawMessage,
        message.file
    )

    constructor(
        sender: String,
        receiver: String,
        rawMessage: String,
        file: String? = null
    ): this (
        sender = sender,
        receiver = receiver,
        rawMessage = rawMessage,
        file = file,
        end = END
            )

    @OptIn(ExperimentalStdlibApi::class)
    override val json: String
        get() {
            val adapter = moshi.adapter<TextMessage>()
            return adapter.toJson(this)
        }

}

package com.ktHat.Messages

import com.ktHat.Models.UnknownMessage
import com.third.lhat.Objects.moshi
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
data class TextMessage constructor(
//    @Json(name = "by") override val sender: User,
//    @Json(name = "to") override val receiver: User,
    /*
        兼容性更改
     */
    @Json(name = "by") override var sender: String,
    @Json(name = "to") override var receiver: String,
    override val type: MessageType = MessageType.TEXT_MESSAGE,
    override val time: Double = getTime(),
    @Json(name = "message") override val rawMessage: String,
    override val file: String? = null,
    //override val end: String = END

) : Message() {
    companion object : Parse() {
        override fun parse(message: UnknownMessage): TextMessage {
            return TextMessage(
                message.sender,
                message.receiver,
                message.type ?: throw NullPointerException(),
                message.time,
                message.rawMessage,
                message.file
            )
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    override val json: String
        get() {
            val adapter = moshi.adapter<TextMessage>()
            return adapter.toJson(this)
        }

    private fun copy() {}
}

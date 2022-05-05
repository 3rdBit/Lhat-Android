package com.ktHat.Messages

import com.ktHat.Statics.END
import com.ktHat.Statics.Objects
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.adapter

/**
 * 此消息的作用是广播一个用户名到服务端。
 * 此消息不应由您手动处理。
 */

@JsonClass(generateAdapter = true)
class UserRegMessage private constructor(
    @Json(name = "by") override val sender: String = "",
    @Json(name = "to") override val receiver: String = "",
    override val type: MessageType = MessageType.USER_NAME,
    override val time: String = "",
    @Json(name = "message") override val rawMessage: String,
    override val file: String? = null,
    override val end: String = END
) : Message() {
    constructor(userName: String): this(rawMessage = userName)

    @OptIn(ExperimentalStdlibApi::class)
    override val json: String
        get() {
            val adapter = Objects.moshi.adapter<UserRegMessage>()
            return adapter.toJson(this)
        }
}
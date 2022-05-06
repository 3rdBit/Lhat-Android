package com.ktHat.Models

import com.ktHat.Messages.Message
import com.ktHat.Messages.MessageType
import com.ktHat.Statics.END
import com.ktHat.Statics.Objects
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.adapter

/**
 * 您永远不应当手动实例化UnknownMessage。
 * 这个类是用来让适配器工作用的，仅此而已。
 */

@JsonClass(generateAdapter = true)
data class UnknownMessage(
    @Json(name = "by") override val sender: String,
    @Json(name = "to") override val receiver: String,
    override val type: MessageType? = null,
    override val time: String,
    @Json(name = "message") override val rawMessage: String,
    override val file: String? = "",
    override val end: String = END
): Message() {

    @OptIn(ExperimentalStdlibApi::class)
    override val json: String
        get() {
            val adapter = Objects.moshi.adapter<UnknownMessage>()
            return adapter.toJson(this)
        }

}
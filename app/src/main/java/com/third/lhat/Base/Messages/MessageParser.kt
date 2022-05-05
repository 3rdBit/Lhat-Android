package com.ktHat.Messages

import com.ktHat.Models.UnknownMessage
import com.ktHat.Statics.Objects.moshi
import com.squareup.moshi.adapter

/**
 * 用法：
 * MessageParser.parse(json: String) - 从json获取Message对象。
 *
 * 您可以不用过度关心包装逻辑。
 */

object MessageParser {
    @OptIn(ExperimentalStdlibApi::class)
    fun parse(json: String): Message {
        val jsonAdapter = moshi.adapter<UnknownMessage>()

        val obj = jsonAdapter.serializeNulls().fromJson(json)

        val message = when (obj?.type) {
            MessageType.TEXT_MESSAGE_ARTICLE -> TextMessage(obj)
            MessageType.USER_MANIFEST -> OnlineListMessage(obj)
            else -> throw UnknownError()
        }

        return message
    }
}
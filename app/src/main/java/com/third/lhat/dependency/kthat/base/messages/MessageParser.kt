package com.ktHat.Messages

import android.util.Log
import com.ktHat.Models.UnknownMessage
import com.third.lhat.Objects.moshi
import com.squareup.moshi.adapter
import com.third.lhat.dependency.kthat.base.messages.DefaultRoomMessage
import com.third.lhat.dependency.kthat.base.messages.EmptyMessage
import com.third.lhat.dependency.kthat.base.messages.FileRecvMessage

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
        Log.d("json", json)
        val obj = jsonAdapter.serializeNulls().fromJson(json)

        val message = when (obj?.type) {
            null -> EmptyMessage()
            MessageType.TEXT_MESSAGE -> TextMessage.parse(obj)
            MessageType.USER_MANIFEST -> OnlineListMessage.parse(obj)
            MessageType.DEFAULT_ROOM -> DefaultRoomMessage.parse(obj)
            MessageType.FILE_RECV_DATA -> FileRecvMessage.parse(obj)
            MessageType.USER_NAME -> throw UnknownError(obj.toString())
        }

        return message
    }
}
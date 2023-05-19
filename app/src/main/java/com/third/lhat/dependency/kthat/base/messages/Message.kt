package com.ktHat.Messages

import com.ktHat.Models.UnknownMessage
import com.ktHat.Utils.getTime
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Message应当是所有消息的父类。
 * 所有子类应当实现json字段的getter。
 * 这意味着您可以在所有Message类中使用message.json来获取对象的json信息。
 */


@JsonClass(generateAdapter = true)
abstract class Message(
    //    @Json(name = "by") override val sender: User,
//    @Json(name = "to") override val receiver: User,
    /*
        兼容性更改
     */
    @Json(name = "by") open var sender: String = "",
    @Json(name = "to") open var receiver: String = "",
    open val type: MessageType? = MessageType.TEXT_MESSAGE,
    open val time: Double = getTime(),
    @Json(name = "message") open val rawMessage: String = "",
    open val file: String? = null,
    //override val end: String = END
) {
    @Json(ignore = true) var isInGroup = false
    var member: String? = null
    abstract val json: String
    abstract class Parse {
        abstract fun parse(message: UnknownMessage): Message
    }
}

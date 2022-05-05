package com.ktHat.Messages

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Message应当是所有消息的父类。
 * 所有子类应当实现json字段的getter。
 * 这意味着您可以在所有Message类中使用message.json来获取对象的json信息。
 */


@JsonClass(generateAdapter = true)
abstract class Message {
    //    abstract val sender: User
    //    abstract val receiver: User
        /*
            兼容性更改
         */
    //    @Json(name = "by") override val sender: User,
    //    @Json(name = "to") override val receiver: User,
    abstract val json: String


    @Json(name = "by")
    open val sender: String = ""


    @Json(name = "to")
    open val receiver: String = ""


    open val type: MessageType? = MessageType.TEXT_MESSAGE_ARTICLE


    open val time: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))


    @Json(name = "message")
    open val rawMessage: String = ""


    open val file: String? = ""


    abstract val end: String
}

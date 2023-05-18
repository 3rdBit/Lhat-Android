package com.ktHat.Messages

import com.squareup.moshi.Json

/**
 * TEXT_MESSAGE_ARTICLE: 普通文本信息
 * USER_MANIFEST: 在线列表
 * USER_NAME: 当前用户名
 */

@Json(name = "type") enum class MessageType {
    TEXT_MESSAGE,
    USER_MANIFEST,
    USER_NAME, // send out
    DEFAULT_ROOM,
    FILE_RECV_DATA,
    //DO_NOT_PROCESS,
}
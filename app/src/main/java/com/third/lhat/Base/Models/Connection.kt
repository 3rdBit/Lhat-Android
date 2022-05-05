package com.ktHat.Models

import com.ktHat.Messages.Message
import com.ktHat.Messages.MessageParser
import com.ktHat.Messages.MessageType
import com.ktHat.Messages.UserRegMessage
import com.ktHat.Statics.Objects.listAdapter
import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.StringWriter
import java.net.InetSocketAddress
import java.net.Socket

/**
 * 用法：
 * Connection(IP: String, port: Int) 获取连接对象。
 *
 * 方法列表：
 * connection.send(x: Message) - 向指定服务端发送信息。
 * connection.startReceiving { x: Message } - 对接收到的信息做处理。
 *      需要注意的是，此方法返回一个Job类型的实例。
 *      您可以操作此协程。
 * connection.close() - 关闭连接。
 */

class Connection(IP: String, port: Int) {

    constructor(IP: String, port: Int, userName: String): this(IP, port) {
        this.send(UserRegMessage(userName))
    }

    private val socket: Socket = Socket()

    init {
        socket.connect(IP, port)

        socket.keepAlive = true
    }

    private fun Socket.connect(IP: String, port: Int) {
        this.connect(InetSocketAddress(IP, port))
    }

    fun close() {
        socket.close()
    }

    fun send(message: Message) {
        val json = message.json
        val dout = socket.getOutputStream()
        try {
            val bytes = "$json\n".toByteArray()
            dout.write(bytes)
            dout.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun startReceiving(
        onMessageChanged: (message: Message) -> Unit,
    ): Job {
        val connection = this
        return GlobalScope.launch() {
            while (true) {
                val received = connection.receive()
                for (json in "\\{.*?\\}".toRegex().findAll(received)) {
                    if (json.value.isBlank()) {
                        continue
                    }
                    val message = MessageParser.parse(json.value)
                    if (message.type == MessageType.USER_MANIFEST) {
                        listAdapter.fromJson(message.rawMessage)?.let { OnlineList.setList(it) }
                        continue
                    }

                    try {
                        onMessageChanged(MessageParser.parse(json.value))
                    } catch (e: JsonDataException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private fun receive(): String {
        val inputStream = socket.getInputStream()
        val writer = StringWriter()
        val reader = BufferedReader(InputStreamReader(inputStream))
        val bt = CharArray(1024)
        do {
            reader.read(bt)
        } while (reader.ready())
        writer.write(bt, 0, bt.size)
        return writer.toString()
    }

}



package com.third.lhat.dependency.kthat.base.models

import android.util.Log
import com.ktHat.Messages.Message
import com.ktHat.Messages.MessageParser
import com.ktHat.Messages.MessageType
import com.ktHat.Messages.UserRegMessage
import com.ktHat.Models.OnlineList
import com.third.lhat.Objects.listAdapter
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
const val TAG = "Connection"
class Connection(IP: String, port: Int, val onClose: () -> Unit, connectTimeout: Int = 3000, soTimeout: Int = 0) {

    constructor(
        IP: String,
        port: Int,
        userName: String,
        onClose: () -> Unit,
        connectTimeout: Int = 3000,
        soTimeout: Int = 0
    ) : this(IP, port, onClose, connectTimeout, soTimeout) {
        this.send(UserRegMessage(userName))
    }

    private val socket: Socket = Socket()

    init {
        socket.soTimeout = soTimeout
        socket.connect(IP, port, connectTimeout)
        socket.keepAlive = true
    }

    private fun Socket.connect(IP: String, port: Int, timeout: Int = 0) {
        this.connect(InetSocketAddress(IP, port), timeout)
    }

    fun close() {
        send("")
        onClose()
        socket.close()
    }

    private fun send(string: String) {
        val dout = socket.getOutputStream()
        try {
            val bytes = string.toByteArray()
            dout.write(bytes)
            dout.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun send(message: Message) {
        val json = message.json
        send(json)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun startReceiving(
        onMessageChanged: (message: Message) -> Unit,
    ): Job {
        val connection = this
        return GlobalScope.launch {
            while (true) {
                val received = connection.receive()
                for (json in "\\{.*?\\}".toRegex().findAll(received)) {
                    if (json.value.isBlank()) {
                        continue
                    }
                    Log.d(TAG, "before try: #JSON=${json.value}")
                    try {
                        val message = MessageParser.parse(json.value)
                        when (message.type) {
                            MessageType.USER_MANIFEST -> {
                                listAdapter.fromJson(message.rawMessage)?.let {
                                    OnlineList.setList(
                                        it
                                    )
                                }
                                continue
                            }
                            MessageType.DEFAULT_ROOM -> {
                                Chat.setGroup(message.rawMessage)
                                continue
                            }
                            else -> { }
                        }

                        onMessageChanged(message)
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



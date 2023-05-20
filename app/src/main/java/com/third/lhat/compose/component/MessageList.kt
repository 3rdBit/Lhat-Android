package com.third.lhat.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.third.lhat.dependency.kthat.base.messages.TextMessage
import com.third.lhat.ViewModel
import com.third.lhat.dependency.kthat.base.models.Chat
import com.third.lhat.normalize

@Composable
fun MessageList(modifier: Modifier = Modifier, padding: Dp = 0.dp) {
    val viewModel: ViewModel = viewModel()
    LazyColumn(
        modifier = modifier,
    ) {
        val chats = Chat.chatMap
        if (chats.isNotEmpty()) {
            items(chats.values.toList()) {  // 消息们
                MessageCard(
                    message = it.lastMessage.let { message ->
                        if (message is TextMessage) {
                            message.normalize(true)
                        } else message
                    },
                    unread = it.unreadMessageNumber,
                    onClick = {
                        viewModel.currentChat = it
                        it.readAllMessage()
                    }
                )
            }
        }
        item {  // 底部留白
            Surface(
                modifier = Modifier
                    .height(padding)
                    .fillMaxWidth()
            ) {}
        }
    }
}
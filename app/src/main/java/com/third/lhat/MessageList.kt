package com.third.lhat

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
import com.third.lhat.kthat.Base.Models.Chat

@Composable
fun MessageList(modifier: Modifier = Modifier, chats: Map<String, Chat>, padding: Dp = 0.dp) {
    val viewModel: ViewModel = viewModel()
    LazyColumn(
        modifier = modifier,
    ) {
        if (chats.isNotEmpty()) {
            items(chats.values.toList()) {  // 消息们
                MessageCard(
                    message = it.lastMessage.normalize(true),
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
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
import com.third.lhat.kthat.Base.Models.Chat

@Composable
fun MessageList(modifier: Modifier = Modifier, chats: Map<String, Chat>, padding: Dp = 0.dp) {
    LazyColumn(
        modifier = modifier,
    ) {
        if (chats.size != 0) {
            items(chats.values.toList()) {  // 消息们
                MessageCard(
                    message = it.lastMessage.normalize(true),
                    unread = it.unreadMessageNumber,
                    onClick = {
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
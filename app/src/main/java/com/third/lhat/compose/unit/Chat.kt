package com.third.lhat.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ktHat.Messages.Message
import com.ktHat.Messages.TextMessage
import com.ktHat.Utils.runOnIO
import com.third.lhat.AppTheme
import com.third.lhat.ViewModel
import com.third.lhat.compose.component.ChatMessage
import com.third.lhat.dependency.kthat.base.models.Chat
import kotlinx.coroutines.launch


@Composable
fun ChatPage(chat: Chat) {
    ChatPage(messageList = chat.messageList)
}

@Composable
fun ChatPage(messageList: SnapshotStateList<Message>) {
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val viewModel: ViewModel = viewModel()
    AppTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.Bottom,
                state = lazyListState,
                modifier = Modifier
                    .weight(1f)
                    .background(Color.LightGray.copy(alpha = 0.3f))
            ) {
                items(messageList) {
                    if (it.rawMessage.isNotEmpty()) {
                        ChatMessage(
                            message = it,
                        )
                    }
                }
            }
            ChattingBar(
                onClickAndClear = { text ->
                    val message = viewModel.currentChat.newTextMessage(text)
//                    messageList += message
                    runOnIO {
                        viewModel.connection?.send(message)
                    }
                    coroutineScope.launch {
                        lazyListState.animateScrollToItem(messageList.size)
                    }
                }
            )
        }
    }
}

@Preview
@Composable
fun ChatPagePreview() {
    val messageList = remember {
        mutableStateListOf<Message>(
            TextMessage(
                sender = "shacha",
                receiver = "ee",
                rawMessage = "hi"
            ),
            TextMessage(
                sender = "ee",
                receiver = "shacha",
                rawMessage = "您好"
            ),
            TextMessage(
                sender = "shacha",
                receiver = "ee",
                rawMessage = "别在这发癫!"
            ),
            TextMessage(
                sender = "ee",
                receiver = "shacha",
                rawMessage = "您好"
            ),
        )
    }
    ChatPage(messageList)
}
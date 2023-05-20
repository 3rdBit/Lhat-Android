package com.third.lhat.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.imePadding
import com.third.lhat.dependency.kthat.base.messages.Message
import com.third.lhat.dependency.kthat.base.messages.MessageType
import com.third.lhat.dependency.kthat.base.messages.TextMessage
import com.third.lhat.dependency.kthat.base.utils.runOnIO
import com.third.lhat.AppTheme
import com.third.lhat.ViewModel
import com.third.lhat.compose.component.ChatMessage
import com.third.lhat.compose.component.TopTab
import com.third.lhat.dependency.kthat.base.messages.EmptyMessage
import com.third.lhat.dependency.kthat.base.models.Chat
import com.third.lhat.dependency.kthat.base.models.isRead
import kotlinx.coroutines.launch


@Composable
fun ChatPage(chat: Chat) {
    ChatPage(messageList = chat.messageList)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChatPage(messageList: SnapshotStateList<Message>) {
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val viewModel: ViewModel = viewModel()
    AppTheme {
        Scaffold(topBar = {
            TopTab(title = viewModel.currentChat.others,
                onBackClick = { viewModel.currentChat = Chat.emptyChat })
        },
            bottomBar = {
                ChattingBar(onClickAndClear = { text ->
                    val message = viewModel.currentChat.newTextMessage(text)
//                    messageList += message
                    runOnIO {
                        viewModel.connection?.send(message)
                    }
                    coroutineScope.launch {
                        lazyListState.animateScrollToItem(messageList.size)
                    }
                })
            },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .imePadding(),
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) { paddingValues ->
            AnimatedVisibility(
                visible = messageList.size == 1 && messageList.first().type == MessageType.EMPTY
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.Black.copy(alpha = 0.07f))
                            .wrapContentWidth()
                            .padding(18.dp, 6.dp),

                        ) {
                        Text(
                            text = "There are no messages yet!", fontSize = 12.sp
                        )
                    }
                    Spacer(modifier = Modifier.size(442.dp))
                }
            }
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                state = lazyListState,
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .consumeWindowInsets(paddingValues)
                    .systemBarsPadding()
                    .background(Color.LightGray.copy(alpha = 0.3f))
            ) {
                items(messageList) {
                    it.isRead = true
                    if (it.rawMessage.isNotEmpty()) {
                        ChatMessage(
                            message = it,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ChatPagePreview() {
    val messageList = remember {
        mutableStateListOf<Message>(
            TextMessage(
                sender = "shacha", receiver = "ee", rawMessage = "hi"
            ),
            TextMessage(
                sender = "ee", receiver = "shacha", rawMessage = "您好"
            ),
            TextMessage(
                sender = "shacha", receiver = "ee", rawMessage = "别在这发癫!"
            ),
            TextMessage(
                sender = "ee", receiver = "shacha", rawMessage = "您好"
            ),
        )
    }
    ChatPage(messageList)
}

@Preview
@Composable
fun EmptyChatPagePreview() {
    val messageList = remember {
        mutableStateListOf<Message>(EmptyMessage())
    }
    ChatPage(messageList)
}
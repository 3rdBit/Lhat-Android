package com.third.lhat.compose.component

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.third.lhat.AppTheme
import com.third.lhat.ViewModel
import com.third.lhat.dependency.kthat.base.messages.Message
import kotlinx.coroutines.launch

@Composable
fun ChatMessage(
    modifier: Modifier = Modifier,
    message: Message,
    contentColorOthers: Color = Color.Black,
    containerColorOthers: CardColors = cardColors(Color.White, contentColorOthers),
    contentColorMe: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    containerColorMe: CardColors = cardColors(
        MaterialTheme.colorScheme.primaryContainer,
        contentColorMe
    )
) {
    val viewModel: ViewModel = viewModel()
    ChatMessage(
        modifier = modifier,
        username = message.member ?: message.sender,
        text = message.rawMessage,
        me = (message.member ?: message.sender) == viewModel.username,
        containerColorOthers = containerColorOthers,
        contentColorOthers = contentColorOthers,
        containerColorMe = containerColorMe,
        contentColorMe = contentColorMe,
        isInGroup = message.isInGroup
    )
}

@Suppress("KotlinConstantConditions")
@Composable
fun ChatMessage(
    modifier: Modifier = Modifier,
    username: String = (viewModel() as ViewModel).username,
    text: String,
    me: Boolean,
    contentColorOthers: Color = Color.Black,
    containerColorOthers: CardColors = cardColors(Color.White, contentColorOthers),
    contentColorMe: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    containerColorMe: CardColors = cardColors(
        MaterialTheme.colorScheme.primaryContainer,
        contentColorMe
    ),
    isInGroup: Boolean
) {
    Row(
        horizontalArrangement = if (me) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom,
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp, 2.dp, 8.dp, 10.dp)
    ) {
        if (me) {
            Message(containerColorMe, me, text, username, contentColorMe, displayUsername = isInGroup)
            Space()
            Favi(username)
        } else {
            Favi(username)
            Space()
            Message(containerColorOthers, me, text, username, contentColorOthers, displayUsername = isInGroup)
        }
    }
}

@Composable
private fun Space() {
    Spacer(
        modifier = Modifier
            .width(8.dp)
    )
}

@Composable
private fun Favi(username: String) {
    Column {
        Favicon(
            name = username,
            shape = CircleShape,
            size = 40.dp,
            normalFontSizeRange = FontSizeRange(10.sp, 15.sp),
            biggerFontSizeRange = FontSizeRange(15.sp, 20.sp)
        )
        Spacer(
            modifier = Modifier
                .height(8.dp)
        )
    }
}

@Composable
private fun Message(
    containerColor: CardColors,
    me: Boolean,
    text: String,
    username: String,
    contentColor: Color,
    displayUsername: Boolean = false
) {
    Column(horizontalAlignment = if (me) Alignment.End else Alignment.Start) {
        if (displayUsername) Text(text = username, Modifier.padding(3.dp, 0.dp, 0.dp, 2.dp))

        Card(
            modifier = Modifier
                .wrapContentSize(),
            colors = containerColor,
        ) {
            Text(
                text = text,
                fontSize = 18.sp,
                color = contentColor,
                modifier = Modifier
                    .padding(10.dp, 6.dp)
                    .widthIn(0.dp, 300.dp)
            )
        }
    }
}

@Preview
@Composable
fun ChatMessageFromMePreview() {
    ChatMessage(username = "shack", text = "test", me = true, isInGroup = false)
}

@Preview
@Composable
fun ChatMessageFromOthersPreview() {
    ChatMessage(username = "shack", text = "test", me = false, isInGroup = false)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Preview
@Composable
fun LongChatMessageColumnPreview() {
    val a = "a"
    val b = "b"
    val messages = remember {
        mutableStateListOf(
            a to "zaima" to Who.THIS,
            b to "不在" to Who.THAT,
            a to "呃呃" to Who.THIS,
            b to "有啥事吗" to Who.THAT,
            a to "没有" to Who.THIS,
            b to "^^" to Who.THAT,
            a to "okok" to Who.THIS,
            a to "okokok" to Who.THIS,
            b to "😅" to Who.THAT,
            a to "别急" to Who.THIS,
            a to "别急" to Who.THIS,
            a to "别急" to Who.THIS,
            a to "别急" to Who.THIS,
            a to "别急" to Who.THIS,
            a to "别急" to Who.THIS,
            a to "别急" to Who.THIS,
            b to "你那个了" to Who.THAT,
            a to "别急" to Who.THIS,
            b to "我急了" to Who.THAT,
            a to "别急" to Who.THIS,
            b to "删好友了" to Who.THAT,
            a to "DONT DO THAT!" to Who.THIS,
            b to "函件会不会说中文啊？" to Who.THAT,
            a to "He was an old man who fished alone in a skiff in the Gulf Stream and he had gone eighty-four days now without taking a fish. In the first forty days a boy had been with him. But after forty days without a fish the boy’s parents had told him that the old man was now definitely and finally salao, which is the worst form of unlucky, and the boy had gone at their orders in another boat which caught three good fish the first week. It made the boy sad to see the old man come in each day with his skiff empty and he always went down to help him carry either the coiled lines or the gaff and harpoon and the sail that was furled around the mast. The sail was patched with flour sacks and, furled, it looked like the flag of permanent defeat." to Who.THIS,
            b to "The old man was thin and gaunt with deep wrinkles in the back of his neck. The brown blotches of the benevolent skin cancer the sun brings from its reflection on the tropic sea were on his cheeks. The blotches ran well down the sides of his face and his hands had the deep-creased scars from handling heavy fish on the cords. But none of these scars were fresh. They were as old as erosions in a fishless desert." to Who.THAT,
        )
    }
    val lazyListState = rememberLazyListState()

    AppTheme {
        val coroutineScope = rememberCoroutineScope()
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        messages += "shack" to "test" to Who.THIS
                        coroutineScope.launch {
                            lazyListState.animateScrollToItem(messages.size)
                        }
                    }
                ) {
                    Text(text = "Add")
                }
            }
        ) { paddingValues ->
            LazyColumn(
                verticalArrangement = Arrangement.Bottom,
                state = lazyListState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color.LightGray.copy(alpha = 0.3f))
            ) {
                messages.forEach { (message, who) ->
                    item {
                        ChatMessage(
                            modifier = Modifier
                                .animateItemPlacement(
                                    animationSpec = tween(200)
                                ),

                            message.first,
                            message.second,
                            who == Who.THIS,
                            isInGroup = true
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ShortChatMessageColumnPreview() {
    val messages = remember {
        mutableStateListOf(
            "shack" to "zaima" to Who.THIS,
            "ocarina" to "1" to Who.THAT,
        )
    }

    val lazyListState = rememberLazyListState()

    AppTheme {
        val coroutineScope = rememberCoroutineScope()
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        messages += "shack" to "test" to Who.THIS
                        coroutineScope.launch {
                            lazyListState.animateScrollToItem(messages.size)
                        }
                    }
                ) {
                    Text(text = "Add")
                }
            }
        ) { paddingValues ->
            LazyColumn(
                verticalArrangement = Arrangement.Bottom,
                state = lazyListState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color.LightGray.copy(alpha = 0.3f))
            ) {
                messages.forEach { (message, who) ->
                    item {
                        ChatMessage(
                            modifier = Modifier
                                .animateItemPlacement(
                                    animationSpec = tween(200)
                                ),
                            message.first,
                            message.second,
                            who == Who.THIS,
                            isInGroup = false
                        )
                    }
                }
            }
        }
    }
}

enum class Who {
    THIS, THAT
}
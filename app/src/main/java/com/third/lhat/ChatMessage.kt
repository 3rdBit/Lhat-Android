package com.third.lhat

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
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
import com.ktHat.Messages.Message
import kotlinx.coroutines.launch

@Composable
fun ChatMessage(
    modifier: Modifier = Modifier,
    message: Message,
    containerColorOthers: Color = Color.White,
    contentColorOthers: Color = Color.Black,
    containerColorMe: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColorMe: Color = MaterialTheme.colorScheme.onPrimaryContainer
) {
    val viewModel: ViewModel = viewModel()
    ChatMessage(
        modifier = modifier,
        username = message.sender,
        text = message.rawMessage,
        me = message.sender == viewModel.username,
        containerColorOthers = containerColorOthers,
        contentColorOthers = contentColorOthers,
        containerColorMe = containerColorMe,
        contentColorMe = contentColorMe
    )
}

@Composable
fun ChatMessage(
    modifier: Modifier = Modifier,
    username: String = (viewModel() as ViewModel).username,
    text: String,
    me: Boolean,
    containerColorOthers: Color = Color.White,
    contentColorOthers: Color = Color.Black,
    containerColorMe: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColorMe: Color = MaterialTheme.colorScheme.onPrimaryContainer
) {
    Row(
        horizontalArrangement = if (me) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom,
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp, 2.dp, 8.dp, 10.dp)
    ) {
        if (me) {
            Message(containerColorMe, text, contentColorMe)
            Space()
            Favi(username)
        } else {
            Favi(username)
            Space()
            Message(containerColorOthers, text, contentColorOthers)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Message(
    containerColor: Color,
    text: String,
    contentColor: Color
) {
    Card(
        modifier = Modifier
            .wrapContentSize(),
        containerColor = containerColor,
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

@Preview
@Composable
fun ChatMessageFromMePreview() {
    ChatMessage(username = "shacha", text = "test", me = true)
}

@Preview
@Composable
fun ChatMessageFromOthersPreview() {
    ChatMessage(username = "shacha", text = "test", me = false)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Preview
@Composable
fun LongChatMessageColumnPreview() {
    val messages = remember {
        mutableStateListOf(
            "shacha" to "zaima" to Who.THIS,
            "naocan" to "不在，cmn" to Who.THAT,
            "shacha" to "我呃呃" to Who.THIS,
            "naocan" to "谔谔有啥事吗" to Who.THAT,
            "shacha" to "你没事吧" to Who.THIS,
            "naocan" to "脑缠，，，" to Who.THAT,
            "shacha" to "okok" to Who.THIS,
            "shacha" to "okokok" to Who.THIS,
            "naocan" to "😅" to Who.THAT,
            "shacha" to "别急" to Who.THIS,
            "shacha" to "别急" to Who.THIS,
            "shacha" to "别急" to Who.THIS,
            "shacha" to "别急" to Who.THIS,
            "shacha" to "别急" to Who.THIS,
            "shacha" to "别急" to Who.THIS,
            "shacha" to "别急" to Who.THIS,
            "naocan" to "你妈死了" to Who.THAT,
            "shacha" to "别急" to Who.THIS,
            "naocan" to "我急了" to Who.THAT,
            "shacha" to "别急" to Who.THIS,
            "naocan" to "删好友了" to Who.THAT,
            "shacha" to "DONT DO THAT!" to Who.THIS,
            "naocan" to "函件会不会说中文啊？" to Who.THAT,
            "shacha" to "He was an old man who fished alone in a skiff in the Gulf Stream and he had gone eighty-four days now without taking a fish. In the first forty days a boy had been with him. But after forty days without a fish the boy’s parents had told him that the old man was now definitely and finally salao, which is the worst form of unlucky, and the boy had gone at their orders in another boat which caught three good fish the first week. It made the boy sad to see the old man come in each day with his skiff empty and he always went down to help him carry either the coiled lines or the gaff and harpoon and the sail that was furled around the mast. The sail was patched with flour sacks and, furled, it looked like the flag of permanent defeat." to Who.THIS,
            "naocan" to "The old man was thin and gaunt with deep wrinkles in the back of his neck. The brown blotches of the benevolent skin cancer the sun brings from its reflection on the tropic sea were on his cheeks. The blotches ran well down the sides of his face and his hands had the deep-creased scars from handling heavy fish on the cords. But none of these scars were fresh. They were as old as erosions in a fishless desert." to Who.THAT,
        )
    }
    val lazyListState = rememberLazyListState()

    AppTheme {
        val coroutineScope = rememberCoroutineScope()
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        messages.plusAssign("shacha" to "test" to Who.THIS)
                        coroutineScope.launch {
                            lazyListState.animateScrollToItem(messages.size)
                        }
                    }
                ) {
                    Text(text = "Add")
                }
            }
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.Bottom,
                state = lazyListState,
                modifier = Modifier
                    .fillMaxSize()
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
                            who == Who.THIS
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
            "shacha" to "zaima" to Who.THIS,
            "naocan" to "1" to Who.THAT,
        )
    }

    val lazyListState = rememberLazyListState()

    AppTheme {
        val coroutineScope = rememberCoroutineScope()
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        messages.plusAssign("shacha" to "test" to Who.THIS)
                        coroutineScope.launch {
                            lazyListState.animateScrollToItem(messages.size)
                        }
                    }
                ) {
                    Text(text = "Add")
                }
            }
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.Bottom,
                state = lazyListState,
                modifier = Modifier
                    .fillMaxSize()
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
                            who == Who.THIS
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
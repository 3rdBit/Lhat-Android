package com.third.lhat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ktHat.Messages.Message
import com.ktHat.Messages.TextMessage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main(messageList: List<Message>) {
    val viewModel: ViewModel = viewModel()
    AppTheme {
        val bottomBarHeight = 48.dp
        val bottomBarHeightPx = with(LocalDensity.current) { bottomBarHeight.roundToPx() }
        val offset: IntOffset by animateIntOffsetAsState(
            IntOffset(
                x = 0,
                y = if (viewModel.bottomTabVisibility) {
                    0
                } else {
                    bottomBarHeightPx
                }
            ),
            animationSpec = spring(
                stiffness = Spring.StiffnessMediumLow,
                visibilityThreshold = IntOffset.VisibilityThreshold
            )
        )
        Scaffold(
            bottomBar = {
                AnimatedVisibility(
                    visible = viewModel.bottomTabVisibility,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it }),
                ) {
                    BottomTab(
                        modifier = Modifier
                            .height(bottomBarHeight)
                    )
                }
            },
            floatingActionButton = {
                FAB(
                    modifier = Modifier
                        .offset { offset }
                )
                { viewModel.bottomTabVisibility = true; viewModel.selectedBar = 0 }
            },
            modifier = Modifier
                .fillMaxSize()
        ) {
            rememberSystemUiController().run {
                setStatusBarColor(
                    MaterialTheme.colorScheme.surface.applyTonalElevation(
                        MaterialTheme.colorScheme,
                        3.dp
                    )
                )
                setSystemBarsColor(
                    MaterialTheme.colorScheme.surface.applyTonalElevation(
                        MaterialTheme.colorScheme,
                        3.dp
                    )
                )
                setNavigationBarColor(
                    MaterialTheme.colorScheme.surface.applyTonalElevation(
                        MaterialTheme.colorScheme,
                        3.dp
                    )
                )
                rememberLazyListState()
                MessageList(
                    messages =  messageList,
                    padding = bottomBarHeight
                )
            }
        }
    }
}

fun generateList(): MutableList<Message> {
    val messageList = mutableListOf<Message>()
    for (number in 0..1000) {
        messageList.add(
            TextMessage(
                sender = number.toString(),
                receiver = number.toString(),
                rawMessage = number.toString().repeat(3),
            )
        )
    }
    return messageList
}


class ComposeActivity : ComponentActivity() {
    companion object : Startable

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            Main()
            LoginPagePreview()
        }
    }
}


@Composable
fun MessageList(modifier: Modifier = Modifier, messages: List<Message>, padding: Dp = 0.dp) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(messages) {  // 消息们
            MessageCard(it)
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

@Preview
@Composable
fun Preview() {
    val messageList = generateList()  // Test list
    Main(messageList)
}

package com.third.lhat

import android.os.Bundle
import android.system.ErrnoException
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ktHat.Models.Connection
import com.third.lhat.kthat.Base.Models.Chat
import java.net.ConnectException
import java.net.UnknownHostException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main(chatList: Map<String, Chat>) {
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
                    chats = chatList,
                    padding = bottomBarHeight
                )
            }
        }
    }
}

class ComposeActivity : ComponentActivity() {
    companion object : Startable

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModel()
        setContent {
            var showMainActivity by remember { mutableStateOf(false) }
            AppTheme() {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    LoginPage(
                        onLoginPressed = { server, port, username ->
                            val connection = Connection(
                                getHost(server),
                                port.toInt(),
                                username
                            )
                            viewModel.username = username
                            connection.startReceiving {
                                Chat.addMessage(it)
                            }
                            connection
                        },
                        catch = { e ->
                            when (e) {
                                is UnknownHostException -> {
                                    e.printStackTrace()
                                    true
                                }
                                is ErrnoException -> {
                                    e.printStackTrace()
                                    true
                                }
                                is ConnectException -> {
                                    e.printStackTrace()
                                    true
                                }
                                else -> {
                                    e.printStackTrace()
                                    true
                                }
                            }
                        },
                        afterConnection = { connection ->
                            showMainActivity = true
                        }
                    )
                    Spacer(
                        modifier = Modifier
                            .height(64.dp)
                            .fillMaxWidth()
                    )
                }
                Box(modifier = Modifier
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        val offset = if (showMainActivity) {
                            0
                        } else {
                            placeable.width
                        }
                        layout(placeable.width, placeable.height) {
                            placeable.placeRelative(offset, 0)
                        }
                    }
                    .fillMaxSize()
                ) {
                    Main(Chat.chatMap)
                }
            }
        }
    }
}


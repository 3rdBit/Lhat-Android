package com.third.lhat.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.third.lhat.Objects
import com.third.lhat.AppTheme
import com.third.lhat.ViewModel
import com.third.lhat.applyTonalElevation
import com.third.lhat.compose.framework.Scaffold
import com.third.lhat.dependency.kthat.base.models.Chat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home() {
    val viewModel: ViewModel = viewModel()
    Objects.composeActivityInstance = LocalViewModelStoreOwner.current
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
            }
            MessageList(
                padding = bottomBarHeight
            )
        }
        Box(
            modifier = Modifier
//                .layout { measurable, constraints ->
//                    val placeable = measurable.measure(constraints)
//                    val chatOffset = if (
//                        viewModel.currentChat != Chat.emptyChat
//                    ) {
//                        0
//                    } else {
//                        placeable.width
//                    }
//                    layout(placeable.width, placeable.height) {
//                        placeable.placeRelative(chatOffset, 0)
//                    }
//                }
                .fillMaxSize()
        ) {
            if (viewModel.currentChat != Chat.emptyChat) {
                ChatPage(viewModel.currentChat)
            }
        }
    }
}
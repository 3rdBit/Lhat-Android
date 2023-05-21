package com.third.lhat.compose.unit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.third.lhat.Objects
import com.third.lhat.ViewModel
import com.third.lhat.applyTonalElevation
import com.third.lhat.compose.BottomTab
import com.third.lhat.compose.ChatPage
import com.third.lhat.compose.FAB
import com.third.lhat.compose.MessageList
import com.third.lhat.compose.component.Favicon
import com.third.lhat.compose.component.TopTab
import com.third.lhat.compose.framework.Scaffold
import com.third.lhat.dependency.kthat.base.models.Chat
import com.third.lhat.theme.ui.AppTheme
import com.third.lhat.theme.ui.LocalTheme
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home() {
    val viewModel: ViewModel = viewModel()
    Objects.composeActivityInstance = LocalViewModelStoreOwner.current
    AppTheme {
        val bottomBarHeight = 48.dp
        val bottomBarHeightPx = with(LocalDensity.current) { bottomBarHeight.roundToPx() }
        val fabOffset: IntOffset by animateIntOffsetAsState(
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
        val chatPageOffsetPercent by animateFloatAsState(
            if (viewModel.currentChat != Chat.emptyChat) 0f else 1f
        )
        var popupVisibility by remember { mutableStateOf(false) }

        val animatedIntOffset by animateIntOffsetAsState(
            targetValue = if (popupVisibility) IntOffset(
                0,
                -150
            ) else IntOffset(0, 0)
        )

        Scaffold(
            topBar = {
                TopTab(
                    title = viewModel.username,
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = LocalTheme.secondaryContainer,
                        scrolledContainerColor = LocalTheme.secondaryContainer.applyTonalElevation(
                            colorScheme = MaterialTheme.colorScheme,
                            elevation = 3.0.dp
                        ),
                        titleContentColor = LocalTheme.onSurface,
                    ),
                    titleSpacerSize = 18.dp,
                    titleFontSize = 20.sp,
                    titleFontFamily = FontFamily.SansSerif,
                    titleFontStyle = FontStyle.Normal,
                    titleFontWeight = FontWeight.Medium,
                    iconSpacerSize = 6.dp,
                    tabHeight = 62.dp,
                ) {
                    Row {
                        Spacer(modifier = Modifier.size(8.dp))
                        IconButton(
                            onClick = { }
                        ) {
                            Favicon(viewModel.username)
                        }
                    }
                }
            },
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
                if (popupVisibility) {
                    OnlineList(
                        onlineList = viewModel.onlineList.filterNot { it == viewModel.username },
                        modifier = Modifier,
                        offset = animatedIntOffset,
                        onDismissRequest = { popupVisibility = false }
                    ) { clicked ->
                        popupVisibility = false
                        viewModel.currentChat = Chat.newChat(clicked)
                    }
                }
            },
            floatingActionButton = {
                FAB(
                    modifier = Modifier
                        .offset { fabOffset }
                )
                {
                    // Online list popup
                    popupVisibility = true
                }
            },
            modifier = Modifier
                .fillMaxSize()
        ) {
            MessageList(
                padding = bottomBarHeight
            )
        }
        Box(
            modifier = Modifier
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)
                    val chatOffset = (placeable.width * chatPageOffsetPercent).roundToInt()

                    layout(placeable.width, placeable.height) {
                        placeable.placeRelative(chatOffset, 0)
                    }
                }
                .fillMaxSize()
        ) {
            if (viewModel.currentChat != Chat.emptyChat) {
                rememberSystemUiController().setSystemBarColor(
                    LocalTheme,
                    statusBarColor = MaterialTheme.colorScheme.surface,
                    navigationBarColor = MaterialTheme.colorScheme.surface
                )
                ChatPage(viewModel.currentChat)
            } else {
                rememberSystemUiController().setSystemBarColor(LocalTheme)
            }
        }
    }
}

private fun SystemUiController.setSystemBarColor(
    LocalTheme: ColorScheme,
    statusBarColor: Color = LocalTheme.secondaryContainer,
    navigationBarColor: Color = LocalTheme.surface
        .applyTonalElevation(
            LocalTheme,
            3.dp
        )
) {
    setStatusBarColor(
        statusBarColor
    )
    setNavigationBarColor(
        navigationBarColor
    )
}

@Preview
@Composable
fun HomePreview() {
    Home()
}
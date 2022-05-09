package com.third.lhat

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import projekt.cloud.piece.c2.pinyin.C2Pinyin.pinyin
import kotlin.math.abs

@Composable
fun AlphabeticBar(
    items: List<String>,
    selectedColor: Color = MaterialTheme.colorScheme.primary,
    baseColor: Color = Color.LightGray,
    headers: List<String>,
    listState: LazyListState
) {

    val offsets = remember { mutableStateMapOf<Int, Float>() }
    var selectedHeaderIndex by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()

    fun updateSelectedIndexIfNeeded(offset: Float) {
        val index = offsets
            .mapValues { abs(it.value - offset) }
            .entries
            .minByOrNull { it.value }
            ?.key ?: return
        if (selectedHeaderIndex == index) return
        selectedHeaderIndex = index
        val selectedItemIndex =
            items.indexOfFirst {
                it.first().pinyin.first().uppercase() == headers[selectedHeaderIndex]
            }
        scope.launch {
            listState.scrollToItem(
                selectedItemIndex
            )
        }
    }

    if (!listState.isOnlyPage()) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .width(32.dp)
                .fillMaxHeight()
                .background(Color.Transparent)
                .alpha(0.8f)
                .clip(RoundedCornerShape(8.dp))
                .pointerInput(Unit) {
                    detectTapGestures {
                        updateSelectedIndexIfNeeded(it.y)
                    }
                }
                .pointerInput(Unit) {
                    detectVerticalDragGestures { change, _ ->
                        updateSelectedIndexIfNeeded(change.position.y)
                    }
                }
        ) {
            headers.forEachIndexed { i, header ->
                Text(
                    header,
                    color = if (items[listState.firstVisibleItemIndex].first().pinyin.first().uppercase() == header)
                        selectedColor
                    else
                        baseColor,
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .onGloballyPositioned {
                            offsets[i] = it.boundsInParent().center.y
                        }
                )
            }
        }
    }
}

@Preview
@Composable
fun AlphabeticBarPreview() {
    val items = mutableListOf("test1", "test2", "test3", "啊啊啊我ptt又掉了", "救救我", "aaawa")
        .run {
            addAll(lastIndex, LoremIpsum().values.first().split(" ").toSet())
            this
        }.sortedBy { it.first().pinyin.first().uppercase() }
    val headers = items.map { it.first().toString() }
    val listState = rememberLazyListState()
    AlphabeticBar(
        items = items,
        headers = headers,
        listState = listState
    )
}
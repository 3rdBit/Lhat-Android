package com.third.lhat

import android.content.res.Resources
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import projekt.cloud.piece.c2.pinyin.C2Pinyin.pinyin
import java.text.Collator
import java.util.*
import kotlin.math.abs

@Composable
fun OnlineList(onlineList: List<String>) {
    val items = remember { onlineList.sortedBy { it.first().pinyin.first().uppercase() } }
    val headers = remember { items.map { it.first().pinyin.first().uppercase() }.toSet().toList() }
    Row {
        val listState = rememberLazyListState()
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items) {
                OnlineLine(it)
            }
        }
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
                items.indexOfFirst { it.first().pinyin.first().uppercase() == headers[selectedHeaderIndex] }
            scope.launch {
                listState.animateScrollToItem(selectedItemIndex)
            }
        }

        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxHeight()
                .background(Color.Gray)
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
                    modifier = Modifier.onGloballyPositioned {
                        offsets[i] = it.boundsInParent().center.y
                    }
                )
            }
        }
    }
}


@Preview
@Composable
fun OnlineLinePrev() {
    OnlineLine(username = "test1")
}

@Preview
@Composable
fun OnlineListPreview() {
    val list = mutableListOf("test1", "test2", "test3", "啊啊啊我ptt又掉了", "救救我", "aaawa")
    OnlineList(list)
}


fun <T> List<T>.sortedByLocale(locale: Locale): List<T> {
    return this.sortedWith { a, b ->
        Collator.getInstance(locale).compare(a, b)
    }
}

fun getLocale(): Locale = Resources.getSystem().configuration.locales[0]

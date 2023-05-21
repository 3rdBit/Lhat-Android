package com.third.lhat.compose.unit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.third.lhat.compose.component.AlphabeticBar
import com.third.lhat.compose.component.OnlineLine
import com.third.lhat.isOnlyPage
import projekt.cloud.piece.c2.pinyin.C2Pinyin.pinyin

@Composable
fun OnlineList(
    onlineList: List<String>,
    modifier: Modifier = Modifier,
    offset: IntOffset = IntOffset.Zero,
    onDismissRequest: (() -> Unit)? = null,
    onClick: (String) -> Unit
) {
    val items = remember { onlineList.sortedBy { it.first().pinyin.first().uppercase() } }
    val headers = remember { items.map { it.first().pinyin.first().uppercase() }.toSet().toList() }
    val listState = rememberLazyListState()
    Popup(
        alignment = Alignment.BottomEnd,
        offset = offset,
        onDismissRequest = onDismissRequest
    )
    {
        List(modifier, listState, items, onClick, headers)
    }
}

@Composable
private fun List(
    modifier: Modifier,
    listState: LazyListState,
    items: List<String>,
    onClick: (String) -> Unit,
    headers: List<String>
) {
    Box(
        contentAlignment = Alignment.TopEnd,
        modifier = modifier
            .wrapContentSize()
            .background(Color.White)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (items.isEmpty()) {
                item {
                    Text(text = "服务器暂时没有其他人")
                }
            } else items(items) {
                OnlineLine(it) {
                    onClick(it)
                }
            }
        }
        if (!listState.isOnlyPage()) {
            AlphabeticBar(
                items = items,
                headers = headers,
                listState = listState
            )
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
fun SingleOnlineListPreview() {
    val list = mutableListOf("test1", "test2", "test3", "test4", "test5", "test6")
    val items = remember { list.sortedBy { it.first().pinyin.first().uppercase() } }
    val headers = remember { items.map { it.first().pinyin.first().uppercase() }.toSet().toList() }
    val listState = rememberLazyListState()
    List(Modifier, listState, items, {  }, headers)
}

@Preview
@Composable
fun MultiOnlineListPreview() {
    val list = mutableListOf("test1", "test2", "test3", "test4", "test5", "test6")
    list.run { addAll(lastIndex, LoremIpsum().values.first().split(" ").toSet()) }
    val items = remember { list.sortedBy { it.first().pinyin.first().uppercase() } }
    val headers = remember { items.map { it.first().pinyin.first().uppercase() }.toSet().toList() }
    val listState = rememberLazyListState()
    List(Modifier, listState, items, {  }, headers)
}

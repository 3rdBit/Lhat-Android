package com.third.lhat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import projekt.cloud.piece.c2.pinyin.C2Pinyin.pinyin

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
        AlphabeticBar(items, headers, listState)
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
    list.run { addAll(lastIndex, LoremIpsum().values.first().split(" ").toSet()) }
    OnlineList(list)
}

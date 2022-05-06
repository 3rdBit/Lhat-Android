package com.third.lhat

import android.content.res.Resources
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import java.text.Collator
import java.util.*

@Composable
fun OnlineList(onlineList: List<String>) {
    LazyColumn() {
        items(onlineList) {
            
        }
    }
}

@Composable
fun OnlineLine(username: String) {
    Row {
        Favicon(
            name = username,
            shape = CircleShape
        )
        Text(
            text = username,
            maxLines = 1,
            textAlign = TextAlign.End,
            fontSize = 40.sp
        )
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
    OnlineList(listOf("test1", "test2", "test3", "啊啊啊我ptt又掉了", "救救我"))
}

fun <T> List<T>.sortedByLocale(locale: Locale): List<T> {
    return this.sortedWith { a, b ->
        Collator.getInstance(locale).compare(a, b)
    }
}

fun getLocale(): Locale = Resources.getSystem().configuration.locales[0]

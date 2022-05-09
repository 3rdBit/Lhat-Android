package com.third.lhat

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ChattingBar() {
    Row {
        TextingField()
    }
}

@Composable
fun TextingField() {
    var message by remember { mutableStateOf("") }
    BasicTextField(
        value = message,
        onValueChange = {
            message = it
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        if (message.isEmpty()) {
            Text(stringResource(R.string.chat_type_hint))
        }
    }
}


@Preview
@Composable
fun ChattingBarPreview() {
    ChattingBar()
}
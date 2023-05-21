package com.third.lhat.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.third.lhat.R

@Composable
fun ChattingBar(
    onClickAndClear: (strMessage: String) -> Unit = {}
) {
    var message by remember { mutableStateOf("") }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        TextingField(
            value = message,
            onValueChange = {
                message = it
            },
            modifier = Modifier
                .weight(7f)
        )
        CircularIconButton(
            onClick = {
                if (message.isNotBlank()) {
                    val origMessage = message
                    message = ""
                    onClickAndClear(origMessage)
                }
            },
            containerColor = Color.Transparent,
            modifier = Modifier
                .size(48.dp)
        ) {
            Icon(
                Icons.Default.Send,
                contentDescription = "Send",
                modifier = Modifier
                    .fillMaxSize(0.5f)
            )

        }
    }
}

@Composable
fun TextingField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = { onValueChange(it) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            disabledTextColor = Color.Transparent
        ),
        placeholder = { Text(stringResource(R.string.chat_type_hint)) },
        modifier = modifier
    )
}


@Preview
@Composable
fun ChattingBarPreview() {
    ChattingBar()
}
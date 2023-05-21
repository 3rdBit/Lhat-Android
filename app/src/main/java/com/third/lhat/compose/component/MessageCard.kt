package com.third.lhat.compose.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.third.lhat.dependency.kthat.base.messages.Message
import com.third.lhat.dependency.kthat.base.messages.TextMessage
import com.third.lhat.theme.ui.AppTheme
import com.third.lhat.ViewModel


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageCard(
    message: Message,
    unread: Int = 0,
    onClick: (/* message: Message */) -> Unit = {},
) {
    val viewModel: ViewModel = viewModel()
//    val touched by remember { mutableStateOf(message in viewModel.selectedMessage) }
    var touched by remember { mutableStateOf(false) }
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier
            .padding(8.dp, 8.dp, 8.dp, 0.dp)
            .clip(RoundedCornerShape(13.dp))
            .background(
                color = if (touched) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.background
            )
            .wrapContentHeight()
            .combinedClickable(
                onLongClick = {
                    if (!viewModel.editing) {
                        viewModel.editing = true
                        viewModel.editingSelectedMessage.add(message)
                        touched = true
                    }
                    if (viewModel.editingSelectedMessage.isEmpty()) {
                        viewModel.editing = false
                    }
                },
                onClick = {
                    if (viewModel.editing) {
                        if (touched) {
                            viewModel.editingSelectedMessage.remove(message)
                        } else {
                            viewModel.editingSelectedMessage.add(message)
                        }
                        touched = !touched
                        if (viewModel.editingSelectedMessage.isEmpty()) {
                            viewModel.editing = false
                        }
                    } else {
                        onClick()
                    }
                }
            )
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .padding(15.dp)
        ) {
            Favicon(
                name = message.sender,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text( //Author
                    text = message.sender,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(
                    modifier = Modifier.height(10.dp)
                )
                Text(
                    text = message.rawMessage,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        if (unread > 0) {
            UnreadBox(
                modifier = Modifier
                    .padding(16.dp, 15.dp),
                unread
            )
        }
    }
}

@Composable
fun UnreadBox(
    modifier: Modifier = Modifier,
    unread: Int
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(
                MaterialTheme.colorScheme.secondaryContainer,
                shape = CircleShape
            )
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                val currentHeight = placeable.height

                layout(placeable.width, currentHeight) {
                    placeable.placeRelative(0, 0)
                }
            }) {

        Text(
            text = unread.toString(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .padding(4.dp)
                .defaultMinSize(30.dp)
        )
    }
}

@Preview
@Composable
fun UnreadBoxPreview() {
    UnreadBox(unread = 5)
}

@Preview
@Composable
fun MessageCardPreview() {
    AppTheme {
        MessageCard(
            TextMessage(
                sender = "what",
                receiver = "shacha",
                rawMessage = "shacha"
            ),
            unread = 1
        )
    }
}
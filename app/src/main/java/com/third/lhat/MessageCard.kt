package com.third.lhat

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ktHat.Messages.Message


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageCard(
    message: Message,
    onClick: (message: Message) -> Unit = {}
) {
    val viewModel = ViewModel()
//    val touched by remember { mutableStateOf(message in viewModel.selectedMessage) }
    var touched by remember { mutableStateOf(false) }
    AppTheme {
        Row(
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
                            viewModel.selectedMessage.add(message)
                            touched = true
                        }
                    },
                    onClick = {
                        if (viewModel.editing) {
                            touched = if (touched) {
                                viewModel.selectedMessage.remove(message)
                                false
                            } else {
                                viewModel.selectedMessage.add(message)
                                true
                            }
                            if (viewModel.selectedMessage.isEmpty()) {
                                viewModel.editing = false
                            }
                        } else {
                            onClick(message)
                        }
                    }
                )


        ) {
            Row(Modifier.padding(15.dp)) {
                Favicon(
                    name = message.sender,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column (
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
        }
    }
}


@Preview
@Composable
fun MessageCardPreview() {
    
}
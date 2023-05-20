package com.third.lhat.compose.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopTab(
    title: String,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
) {
    TopAppBar(
        title = {
            Column {
                Spacer(
                    modifier = Modifier
                        .size(16.dp)
                )
                Text(
                    text = title,
                    fontSize = 18.sp
                )
            }
        },
        navigationIcon = {
            Column {
                Spacer(
                    modifier = Modifier
                        .size(4.dp)
                )
                IconButton(
                    onClick = onBackClick
                ) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back")
                }
            }
        },
        modifier = modifier
            .height(56.dp)
    )
}
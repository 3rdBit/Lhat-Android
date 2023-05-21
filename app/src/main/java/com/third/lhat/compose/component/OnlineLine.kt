package com.third.lhat.compose.component

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OnlineLine(
    username: String,
    onClick: () -> Unit = {}
) {
    DropdownMenuItem(
        text = {
            Text(
                text = username,
                maxLines = 1,
                fontSize = 18.sp
            )
        },
        onClick = onClick,
        leadingIcon = {
            Favicon(
                name = username,
                shape = CircleShape,
                size = 32.dp,
                normalFontSizeRange = FontSizeRange(5.sp, 10.sp),
                biggerFontSizeRange = FontSizeRange(10.sp, 15.sp)
            )
        }
    )
}
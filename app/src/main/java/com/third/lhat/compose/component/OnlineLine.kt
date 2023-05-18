package com.third.lhat.compose

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.third.lhat.compose.component.Favicon
import com.third.lhat.compose.component.FontSizeRange

@Composable
fun OnlineLine(username: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
    ) {
        Favicon(
            name = username,
            shape = CircleShape,
            size = 32.dp,
            normalFontSizeRange = FontSizeRange(5.sp, 10.sp),
            biggerFontSizeRange = FontSizeRange(10.sp, 15.sp)
        )
        Spacer(
            modifier = Modifier
                .width(10.dp)
        )
        Text(
            text = username,
            maxLines = 1,
            fontSize = 18.sp
        )
    }
}
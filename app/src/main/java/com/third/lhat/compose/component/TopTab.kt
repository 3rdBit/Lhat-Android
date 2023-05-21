package com.third.lhat.compose.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopTab(
    title: String,
    modifier: Modifier = Modifier,
    titleSpacerSize: Dp = 16.dp,
    titleFontSize: TextUnit = 18.sp,
    titleFontStyle: FontStyle? = null,
    titleFontWeight: FontWeight? = null,
    titleFontFamily: FontFamily? = null,
    iconSpacerSize: Dp = 4.dp,
    tabHeight: Dp = 56.dp,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    icon: @Composable (() -> Unit)
) {
    TopAppBar(
        title = {
            Column {
                Spacer(
                    modifier = Modifier
                        .size(titleSpacerSize)
                )
                Text(
                    text = title,
                    fontSize = titleFontSize,
                    fontStyle = titleFontStyle,
                    fontFamily = titleFontFamily,
                    fontWeight = titleFontWeight
                )
            }
        },
        navigationIcon = {
            Column {
                Spacer(
                    modifier = Modifier
                        .size(iconSpacerSize)
                )
                icon()
            }
        },
        colors = colors,
        modifier = modifier
            .height(tabHeight)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediumTopTab(
    title: String,
    modifier: Modifier = Modifier,
    onButtonClick: () -> Unit = {},
    titleSpacerSize: Dp = 16.dp,
    titleFontSize: TextUnit = 18.sp,
    iconSpacerSize: Dp = 4.dp,
    tabHeight: Dp = 56.dp,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    icon: @Composable (() -> Unit)
) {
    MediumTopAppBar(
        title = {
            Text(
                text = title,
                fontSize = titleFontSize
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onButtonClick
            ) {
                icon()
            }
        },
        colors = colors,
        modifier = modifier
    )
}
package com.third.lhat

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun BottomTab(modifier: Modifier = Modifier) {
    val viewModel: ViewModel = viewModel()
    CompositionLocalProvider(
        LocalRippleTheme provides ClearRippleTheme
    ) {
        NavigationBar(modifier = modifier) {
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Home, contentDescription = null) },
                selected = viewModel.selectedBar == 0,
                onClick = { viewModel.selectedBar = 0; viewModel.bottomTabVisibility = true },
                modifier = Modifier.noRippleClickable { viewModel.selectedBar = 0 }
            )
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Settings, contentDescription = null) },
                selected = viewModel.selectedBar == 1,
                onClick = { viewModel.selectedBar = 1; viewModel.bottomTabVisibility = false },
                modifier = Modifier.noRippleClickable { viewModel.selectedBar = 1 }
            )
        }
    }
}

@Preview
@Composable
fun BottomTabPreview() {
    BottomTab()
}
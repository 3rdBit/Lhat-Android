package com.third.lhat.compose.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.third.lhat.R
import com.third.lhat.ViewModel

@Composable
@Suppress("FunctionName")
fun FAB(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val viewModel: ViewModel = viewModel()
    ExtendedFloatingActionButton(
        onClick = { onClick() },
        icon = { Icon(Icons.Filled.Add, stringResource(R.string.fab)) },
        text = { Text(text = stringResource(R.string.fab) ) },
        modifier = modifier,
        expanded = viewModel.bottomTabVisibility,
    )
}

@Preview
@Composable
fun FABPreview() {
    FAB(onClick = {})
}
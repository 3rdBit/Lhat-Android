package com.third.lhat

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ktHat.Models.Connection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URI
import kotlin.coroutines.EmptyCoroutineContext

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginPage(
    onLoginPressed: (server: String, port: String, username: String) -> Connection,
    catch: (e: Exception) -> Boolean,
    afterConnection: (connection: Connection) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    var server by remember { mutableStateOf("") }

    var port by remember { mutableStateOf("") }

    var username by remember { mutableStateOf("") }

    var usernameLimitExceeded by remember { mutableStateOf(false) }

    val isFocused = remember { mutableStateListOf(false, false, false) }

    var pressed by remember { mutableStateOf(false) }

    val hintEnd by animateIntAsState(
        if (usernameLimitExceeded) {
            stringResource(R.string.username_limit_exceeded).lastIndex  //第十二个字符为最后一个字符
        } else {
            integerResource(R.integer.username_limit_exceeded_index)
        }
    )

    val usernameBackgroundColor by animateColorAsState(
        if (usernameLimitExceeded) {
            colorScheme.errorContainer
        } else if (isFocused[0]) {
            colorScheme.primaryContainer
        } else {
            colorScheme.background
        }
    )

    val serverIpBackgroundColor by animateColorAsState(
        if (isFocused[1]) {
            colorScheme.primaryContainer
        } else {
            colorScheme.background
        }
    )

    val serverPortBackgroundColor by animateColorAsState(
        if (isFocused[2]) {
            colorScheme.primaryContainer
        } else {
            colorScheme.background
        }
    )

    val confirmButtonBackgroundColor by animateColorAsState(
        if (server.isBlank() ||
            port.isBlank() ||
            username.isBlank() ||
            usernameLimitExceeded
        ) {
            colorScheme.surfaceVariant
        } else {
            colorScheme.primaryContainer
        }
    )

    val confirmButtonIconColor by animateColorAsState(
        if (server.isBlank() ||
            port.isBlank() ||
            username.isBlank() ||
            usernameLimitExceeded
        ) {
            colorScheme.onSurfaceVariant
        } else {
            colorScheme.primary
        }
    )
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val focusManager = LocalFocusManager.current
        OutlinedTextField(
            isError = (usernameLimitExceeded),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = usernameBackgroundColor
            ),
            label = {
                Text(text = stringResource(R.string.username_limit_exceeded).substring(0..hintEnd))
            },
            value = username,
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusEvent {
                    isFocused[0] = it.isFocused
                },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            onValueChange = {
                usernameLimitExceeded = it
                    .replace("[\u4E00-\u9FA5]".toRegex(), "")
                    .length.run { this + (it.length - this) * 2 } > 20
                username = it.replace("[^\\u4E00-\\u9FA5A-Za-z0-9_]+".toRegex(), "")
            })
        Spacer(Modifier.size(16.dp))
        Row() {
            OutlinedTextField(
                label = { Text(stringResource(R.string.server_host_hint)) },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = serverIpBackgroundColor
                ),
                modifier = Modifier
                    .weight(1.618f)
                    .onFocusEvent {
                        isFocused[1] = it.isFocused
                    },
                value = server,
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Ascii),
                onValueChange = {
                    if (it.contains("(?<!https?)[:：]".toRegex())) {

                        val split = it.split("(?<!https?)[:：]".toRegex()).toMutableList()
                        server = split[0]
                        split.removeIf { it.isBlank() }
                        if (split.size > 1) {
                            port = split[1]
                        } else {
                            focusManager.moveFocus(FocusDirection.Right)
                        }
                    } else {
                        server = it
                    }
                }
            )
            Spacer(Modifier.size(8.dp))
            OutlinedTextField(
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = serverPortBackgroundColor
                ),
                modifier = Modifier
                    .onFocusEvent {
                        isFocused[2] = it.isFocused
                    }
                    .weight(1f),
                label = { Text(stringResource(R.string.server_port_hint)) },
                value = port,
                shape = RoundedCornerShape(14.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
                onValueChange = {
                    port = it.filter { it.isDigit() }.take(5)
                }
            )
        }
        Spacer(Modifier.size(96.dp))
        CompositionLocalProvider(
            LocalRippleTheme provides ClearRippleTheme
        ) {
            CircularIconButton(
                modifier = Modifier
                    .size(100.dp),
                containerColor = confirmButtonBackgroundColor,
                onClick = if (!pressed) {
                    if (
                        server.isBlank() ||
                        port.isBlank() ||
                        username.isBlank() ||
                        usernameLimitExceeded
                    ) {
                        {}
                    } else {
                        {
                            pressed = true
                            var isError = false
                            val coroutineScope = CoroutineScope(EmptyCoroutineContext)
                            coroutineScope.launch(Dispatchers.IO) {
                                try {
                                    val connection = onLoginPressed(server, port, username)
                                    if (!isError) {
                                        afterConnection(connection)
                                    }
                                } catch (e: Exception) {
                                    isError = catch(e)
                                    pressed = false
                                }
                            }
                        }
                    }
                } else {
                    {}
                }
            ) {
                Icon(
                    tint = confirmButtonIconColor,
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(R.string.connect_to_server),
                    modifier = Modifier
                        .fillMaxSize(0.5f)
                )
            }
        }
    }
}

fun getHost(string: String): String {
    val uri = URI(string)
    return uri.host ?: string
}

@Preview
@Composable
fun LoginPagePreview() {
    AppTheme() {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(
                modifier = Modifier
                    .height(64.dp)
                    .fillMaxWidth()
            )
        }
    }

}
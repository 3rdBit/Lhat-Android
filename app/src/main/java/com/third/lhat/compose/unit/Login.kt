package com.third.lhat.compose

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.third.lhat.dependency.kthat.base.models.Connection
import com.ktHat.Utils.runOnIO
import com.ktHat.Utils.runOnMain
import com.third.lhat.AppTheme
import com.third.lhat.ClearRippleTheme
import com.third.lhat.R
import java.net.URI

@Composable
fun LoginPage(
    onLoginPressed: (server: String, port: String, username: String) -> Connection,
    onError: (e: Exception) -> Unit,
    afterConnection: (connection: Connection, server: String, port: String, username: String) -> Unit,
) {
    val colorScheme = MaterialTheme.colorScheme

    var server by remember { mutableStateOf("") }

    var port by remember { mutableStateOf("") }

    var username by remember { mutableStateOf("") }

    val isFocused = remember { mutableStateListOf(false, false, false) }

    var loginPressed by remember { mutableStateOf(false) }

    var displayError by remember { mutableStateOf(false) }

    val errors = remember {
        mutableStateMapOf<Field, List<Error>>(
            Field.USERNAME to listOf(Error.FIELD_IS_EMPTY),
            Field.PORT to listOf(Error.FIELD_IS_EMPTY),
            Field.SERVER to listOf(Error.FIELD_IS_EMPTY)
        )
    }

    val hintEnd by animateIntAsState(
        if (errors[Field.USERNAME]!!.contains(Error.LENGTH_EXCEEDED)) {
            stringResource(R.string.username_limit_exceeded).lastIndex  //第十二个字符为最后一个字符
        } else {
            integerResource(R.integer.username_limit_exceeded_index)
        }
    )

    val usernameBackgroundColor by animateColorAsState(
        if (isFocused[0]) {
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
        if (errors.values.any { it.isNotEmpty() }) {
            colorScheme.surfaceVariant
        } else {
            colorScheme.primaryContainer
        }
    )

    val confirmButtonIconColor by animateColorAsState(
        if (errors.values.any { it.isNotEmpty() }) {
            colorScheme.onSurfaceVariant
        } else {
            colorScheme.primary
        }
    )
    Column(
        modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val focusManager = LocalFocusManager.current
        OutlinedTextField(isError = displayError && errors[Field.USERNAME]!!.isNotEmpty(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = usernameBackgroundColor,
                unfocusedContainerColor = usernameBackgroundColor,
            ),
            label = {
                if (displayError && errors[Field.USERNAME]!!.contains(Error.FIELD_IS_EMPTY)) {
                    Text(
                        text = stringResource(R.string.username_empty),
                        Modifier.background(Color.Transparent)
                    )
                } else {
                    Text(
                        text = stringResource(R.string.username_limit_exceeded).substring(0..hintEnd),
                        Modifier.background(Color.Transparent)
                    )
                }
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
                val usernameLimitExceeded = it.replace(
                    "[\u4E00-\u9FA5]".toRegex(), ""
                ).length.run { this + (it.length - this) * 2 } > 20
                if (usernameLimitExceeded) errors[Field.USERNAME] =
                    errors[Field.USERNAME]!! + Error.LENGTH_EXCEEDED
                if (errors[Field.USERNAME]!!.contains(Error.FIELD_IS_EMPTY)) {
                    errors[Field.USERNAME] = errors[Field.USERNAME]!! - Error.FIELD_IS_EMPTY
                }
                username = it.replace("[^\\u4E00-\\u9FA5A-Za-z0-9_]+".toRegex(), "")
            })
        Spacer(Modifier.size(16.dp))
        Row {
            OutlinedTextField(isError = displayError && errors[Field.SERVER]!!.isNotEmpty(),
                label = {
                    if (displayError && errors[Field.SERVER]!!.contains(Error.FIELD_IS_EMPTY)) {
                        Text(
                            stringResource(R.string.server_host_empty),
                            Modifier.background(Color.Transparent)
                        )
                    } else {
                        Text(
                            stringResource(R.string.server_host_hint),
                            Modifier.background(Color.Transparent)
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = serverIpBackgroundColor,
                    unfocusedContainerColor = serverIpBackgroundColor,
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
                onValueChange = { str ->
                    if (errors[Field.SERVER]!!.contains(Error.FIELD_IS_EMPTY)) {
                        errors[Field.SERVER] = errors[Field.SERVER]!! - Error.FIELD_IS_EMPTY
                    }
                    if (str.contains("(?<!https?)[:：]".toRegex())) {
                        val split = str.split("(?<!https?)[:：]".toRegex()).toMutableList()
                        server = split[0]
                        split.removeIf { it.isBlank() }
                        if (split.size > 1) {
                            port = split[1]
                            if (errors[Field.PORT]!!.contains(Error.FIELD_IS_EMPTY)) {
                                errors[Field.PORT] = errors[Field.PORT]!! - Error.FIELD_IS_EMPTY
                            }
                        } else {
                            focusManager.moveFocus(FocusDirection.Right)
                        }
                    } else {
                        server = str
                    }
                })
            Spacer(Modifier.size(8.dp))
            OutlinedTextField(isError = displayError && errors[Field.PORT]!!.isNotEmpty(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = serverPortBackgroundColor,
                    unfocusedContainerColor = serverPortBackgroundColor,
                ),
                modifier = Modifier
                    .onFocusEvent {
                        isFocused[2] = it.isFocused
                    }
                    .weight(1f),
                label = {
                    if (displayError && errors[Field.PORT]!!.contains(Error.FIELD_IS_EMPTY)) {
                        Text(
                            stringResource(R.string.server_port_empty),
                            Modifier.background(Color.Transparent)
                        )
                    } else {
                        Text(
                            stringResource(R.string.server_port_hint),
                            Modifier.background(Color.Transparent)
                        )
                    }
                },
                value = port,
                shape = RoundedCornerShape(14.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
                onValueChange = { text ->
                    val _port = text.filter { it.isDigit() }.take(5)
                    port = _port
                    if (text != _port) {
                        return@OutlinedTextField
                    }
                    if (errors[Field.PORT]!!.contains(Error.FIELD_IS_EMPTY)) {
                        errors[Field.PORT] = errors[Field.PORT]!! - Error.FIELD_IS_EMPTY
                    }
                })
        }
        Spacer(Modifier.size(96.dp))
        CompositionLocalProvider(
            LocalRippleTheme provides ClearRippleTheme
        ) {
            val isIllegal = {
                server.isBlank() || port.isBlank() || username.isBlank() || errors[Field.USERNAME]!!.contains(
                    Error.LENGTH_EXCEEDED
                )
            }
            CircularIconButton(modifier = Modifier.size(100.dp),
                containerColor = confirmButtonBackgroundColor,
                onClick = if (!loginPressed) {
                    if (isIllegal()) {
                        lambda@{
                            if (server.isBlank()) {
                                errors[Field.SERVER] = errors[Field.SERVER]!! + Error.FIELD_IS_EMPTY
                            }
                            if (port.isBlank()) {
                                errors[Field.PORT] = errors[Field.PORT]!! + Error.FIELD_IS_EMPTY
                            }
                            if (username.isBlank()) {
                                errors[Field.USERNAME] =
                                    errors[Field.USERNAME]!! + Error.FIELD_IS_EMPTY
                            }
                            displayError = true
                        }
                    } else {
                        {
                            loginPressed = true
                            //error here
                            runOnIO {
                                try {
                                    val connection = onLoginPressed(server, port, username)
                                    afterConnection(connection, server, port, username)
                                } catch (e: Exception) {
                                    runOnMain {
                                        loginPressed = false
                                        onError(e)
                                    }
                                }
                            }
                        }
                    }
                } else {
                    {}
                }) {
                Icon(
                    tint = confirmButtonIconColor,
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(R.string.connect_to_server),
                    modifier = Modifier.fillMaxSize(0.5f)
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
    AppTheme {
        Column(
            verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()
        ) {
            Spacer(
                modifier = Modifier
                    .height(64.dp)
                    .fillMaxWidth()
            )
        }
    }

}

private enum class Field {
    SERVER, USERNAME, PORT
}

private enum class Error {
    FIELD_IS_EMPTY, LENGTH_EXCEEDED
}
import android.system.ErrnoException
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ktHat.Models.Connection
import com.third.lhat.Objects
import com.third.lhat.ActivityCollector
import com.third.lhat.AppTheme
import com.third.lhat.ViewModel
import com.third.lhat.compose.Home
import com.third.lhat.compose.LoginPage
import com.third.lhat.compose.getHost
import com.third.lhat.dependency.kthat.base.models.Chat
import java.net.ConnectException
import java.net.UnknownHostException

@Suppress("FunctionName")
fun Main() = @Composable {
    var showMainActivity by remember { mutableStateOf(false) }
    AppTheme {
        val viewModel: ViewModel = viewModel()
        Objects.viewModel = viewModel
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            LoginPage(
                onLoginPressed = { server, port, username ->
                    val connection = Connection(
                        getHost(server),
                        port.toInt(),
                        username,
                        onClose = { ActivityCollector.removeAll() }
                    )
                    viewModel.username = username
                    connection.startReceiving {
                        Chat.addMessage(it)
                    }
                    connection
                },
                onError = { e ->
                    when (e) {
                        is UnknownHostException -> {
                            e.printStackTrace()

                        }
                        is ErrnoException -> {
                            e.printStackTrace()

                        }
                        is ConnectException -> {
                            e.printStackTrace()

                        }
                        else -> {
                            e.printStackTrace()

                        }
                    }
                },
                afterConnection = { connection ->
                    viewModel.connection = connection
                    showMainActivity = true
                },
            )
            Spacer(
                modifier = Modifier
                    .height(64.dp)
                    .fillMaxWidth()
            )
        }
        Box(modifier = Modifier
//                    .layout { measurable, constraints ->
//                        val placeable = measurable.measure(constraints)
//                        val offset = if (showMainActivity) {
//                            0
//                        } else {
//                            placeable.width
//                        }
//                        layout(placeable.width, placeable.height) {
//                            placeable.placeRelative(offset, 0)
//                        }
//                    }
            .fillMaxSize(),
        ) {
            if (showMainActivity) {
                Home()
            }
        }
    }
}
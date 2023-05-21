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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.third.lhat.dependency.kthat.base.utils.runOnMain
import com.third.lhat.dependency.kthat.base.models.Connection
import com.third.lhat.ActivityCollector
import com.third.lhat.theme.ui.AppTheme
import com.third.lhat.Constants
import com.third.lhat.Database
import com.third.lhat.Objects
import com.third.lhat.ViewModel
import com.third.lhat.compose.unit.Home
import com.third.lhat.compose.unit.LoginPage
import com.third.lhat.compose.unit.getHost
import com.third.lhat.database.model.Server
import com.third.lhat.database.model.User
import com.third.lhat.dependency.kthat.base.models.Chat
import com.third.lhat.makeToast
import java.net.ConnectException
import java.net.UnknownHostException
import java.time.LocalDateTime

@Suppress("FunctionName")
fun Main() = @Composable { ->
    var showMainActivity by remember { mutableStateOf(false) }
    AppTheme {
        val viewModel: ViewModel = viewModel()
        val context = LocalContext.current

        Objects.viewModel = viewModel
        Column(
            verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()
        ) {
            LoginPage(
                onLoginPressed = { server, port, username ->
                    val connection = Connection(
                        getHost(server),
                        port.toInt(),
                        username,
                        onClose = { ActivityCollector.removeAll() })
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
                            makeToast(context, "未知主机，也许是服务器填写错误")
                        }

                        is ErrnoException -> {
                            e.printStackTrace()
                            makeToast(context, "socket出现错误：${e.localizedMessage}")
                        }

                        is ConnectException -> {
                            e.printStackTrace()
                            makeToast(context, "连接失败：${e.localizedMessage}")
                        }

                        else -> {
                            e.printStackTrace()
                            makeToast(context, "${e.javaClass.canonicalName}：${e.localizedMessage}")
                        }
                    }
                },
            ) { connection, server, port, username ->
                try {
                    addUserServerToDB(server, port, username)
                } catch (e: Exception) {
                    e.printStackTrace()
                    runOnMain {
                        makeToast(context, "数据库出现错误：${e.localizedMessage}")
                    }
                }
                viewModel.connection = connection
                showMainActivity = true
            }
            Spacer(
                modifier = Modifier
                    .height(64.dp)
                    .fillMaxWidth()
            )
        }
        Box(
            modifier = Modifier
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

private fun addUserServerToDB(
    server: String,
    port: String,
    username: String,
    time: LocalDateTime = LocalDateTime.now(),
) {
    val db = Database.db
    val userId = db.userDao().queryOrInsert(
        User(
            userId = Constants.ID.NOT_SET, username = username
        )
    )
    db.serverDao().updateOrInsert(
        Server(
            serverId = Constants.ID.NOT_SET,
            address = server,
            port = port.toInt(),
            lastLogin = time,
            serverUserId = userId
        )
    )
}

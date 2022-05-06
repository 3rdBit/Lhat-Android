package com.third.lhat

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ktHat.Messages.Message
import com.ktHat.Messages.TextMessage
import java.io.Serializable
import kotlin.math.ln

fun Color.applyTonalElevation(colorScheme: ColorScheme, elevation: Dp): Color {
    return if (this == colorScheme.surface) {
        colorScheme.surfaceColorAtElevation(elevation)
    } else {
        this
    }
}

fun ColorScheme.surfaceColorAtElevation(
    elevation: Dp,
): Color {
    if (elevation == 0.dp) return surface
    val alpha = ((4.5f * ln(elevation.value + 1)) + 2f) / 100f
    return surfaceTint.copy(alpha = alpha).compositeOver(surface)
}

fun Int.dp() = android.util.TypedValue.applyDimension(
    android.util.TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics
).toInt()

inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

interface Startable {
    fun start(
        context: Context,
        launcher: ActivityResultLauncher<Intent>? = null,
        data: Map<String, Any>? = null
    ) {
        val intent = Intent(
            context,
            this::class.java.classLoader?.loadClass(
                this::class.java.name.replace(
                    "\$Companion",
                    ""
                )
            )
        )
        data?.forEach {
            intent.putExtra(it.key, it.value)
        }
        launcher?.launch(intent) ?: context.startActivity(intent)
    }
}


private fun Intent.putExtra(first: String, second: Any) {
    when (second) {
        is Bundle -> this.putExtra(first, second)
        is Parcelable -> this.putExtra(first, second)
        is Serializable -> this.putExtra(first, second)
        is Array<*> -> this.putExtra(first, second)
        is Boolean -> this.putExtra(first, second)
        is BooleanArray -> this.putExtra(first, second)
        is Number -> this.putExtra(first, second)
        is ByteArray -> this.putExtra(first, second)
        is CharArray -> this.putExtra(first, second)
        is CharSequence -> this.putExtra(first, second)
        is DoubleArray -> this.putExtra(first, second)
        is FloatArray -> this.putExtra(first, second)
        is IntArray -> this.putExtra(first, second)
        is LongArray -> this.putExtra(first, second)
        is ShortArray -> this.putExtra(first, second)
        is String -> this.putExtra(first, second)
    }
}

fun <T> SnapshotStateList<T>.flush() {
    this.add(0, this[0])
    this.removeAt(0)
}

fun Message.normalize(minimize: Boolean = true): Message {
    val rawMessage = this.rawMessage.normalize().run {
        if (minimize) {
            this.minimize()
        } else {
            this
        }
    }
    return when (this) {
        is TextMessage -> this.copy(rawMessage = rawMessage)
        else -> throw UnknownError("Unknown Message Type! ")
    }
}

fun String.normalize(): String {
    return this.filter {
        it.isISOControl() or
                it.isDefined() or
                it.isIdentifierIgnorable()
    }
        .replace("\\p{C}".toRegex(), "")
}

fun String.minimize(
    characters: Int = 32,
    endsTo: String = "…"
): String {
    val stringBuilder = StringBuilder()
    var counter = 0
    run {
        this.forEach {
            counter += if (it.isASCII()) 1 else 2
            stringBuilder.append(it)
            if (counter >= characters) {
                return@run
            }
        }
    }
    if (stringBuilder.length < this.length) {
        stringBuilder.append(endsTo)
    }
    return stringBuilder.toString()
}
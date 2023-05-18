package com.third.lhat.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.third.lhat.dependency.colorHash.ColorHash
import com.third.lhat.isASCII
import de.hammwerk.material.color.colorspaces.RgbColor
import de.hammwerk.material.color.util.createPalette
import de.hammwerk.material.color.util.toRgbColorOrNull

@Composable
fun Favicon(
    name: String,
    modifier: Modifier = Modifier,
    size: Dp = 60.dp,
    normalFontSizeRange: FontSizeRange = FontSizeRange(
        min = 10.sp,
        max = 20.sp
    ),
    biggerFontSizeRange: FontSizeRange = FontSizeRange(
        min = 15.sp,
        max = 35.sp
    ),
    shape: Shape = RoundedCornerShape(15.dp)
) {
    var isBigger = false
    var letter = name.split(" ")[0]
    if (!(letter.isASCII()) or (letter.length > 4)) {
        letter = letter[0].toString().uppercase()
        isBigger = true
    }
    val colorPalette = ColorHash(name).toHexString().toRgbColorOrNull()?.createPalette()

    val colorBg: RgbColor = colorPalette?.get(5) ?: RgbColor(242.0, 243.0, 245.0)

    val colorText = colorPalette?.get(0) ?: RgbColor(65.0, 66.0, 68.0)

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(shape)
            .size(size)
            .background(
                Color(
                    colorBg.red.toFloat(),
                    colorBg.green.toFloat(),
                    colorBg.blue.toFloat()
                )
            )
    ) {
        AutoSizeText(
            text = letter,
            textAlign = TextAlign.Center,
            color = Color(
                colorText.red.toFloat(),
                colorText.green.toFloat(),
                colorText.blue.toFloat()
            ),
            fontSizeRange = if (!isBigger)
                normalFontSizeRange
            else
                biggerFontSizeRange,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun AutoSizeText(
    text: String,
    fontSizeRange: FontSizeRange,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    style: TextStyle = LocalTextStyle.current,
) {
    var fontSizeValue by remember { mutableStateOf(fontSizeRange.max.value) }
    var readyToDraw by remember { mutableStateOf(false) }

    Text(
        text = text,
        color = color,
        maxLines = maxLines,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        style = style,
        fontSize = fontSizeValue.sp,
        onTextLayout = {
            if (it.didOverflowHeight && !readyToDraw) {
                val nextFontSizeValue = fontSizeValue - fontSizeRange.step.value
                if (nextFontSizeValue <= fontSizeRange.min.value) {
                    fontSizeValue = fontSizeRange.min.value
                    readyToDraw = true
                } else {
                    fontSizeValue = nextFontSizeValue
                }
            } else {
                readyToDraw = true
            }
        },
        modifier = modifier.drawWithContent { if (readyToDraw) drawContent() }
    )
}

data class FontSizeRange(
    val min: TextUnit,
    val max: TextUnit,
    val step: TextUnit = DEFAULT_TEXT_STEP,
) {
    init {
        require(min < max) { "min should be less than max, $this" }
        require(step.value > 0) { "step should be greater than 0, $this" }
    }

    companion object {
        private val DEFAULT_TEXT_STEP = 1.sp
    }
}

@Preview
@Composable
fun FaviconPreview1() {
    Favicon(name = "shacha")  // 预期：S
}

@Preview
@Composable
fun FaviconPreview2() {
    Favicon(name = "John Ciena")  // 预期：John
}

@Preview
@Composable
fun FaviconPreview3() {
    Favicon(name = "Rick Alsty")  // 预期：Rick
}

@Preview
@Composable
fun FaviconPreview4() {
    Favicon(name = "测试")  // 预期：测
}
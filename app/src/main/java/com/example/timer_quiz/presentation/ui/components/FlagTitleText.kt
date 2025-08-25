package com.example.timer_quiz.presentation.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timer_quiz.presentation.ui.theme.FlagsColors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.toArgb


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FlagTitleText(
    text: String,
    fillColor: Color = FlagsColors.OrangePrimary,
    borderColor: Color = Color.Black,
    shadowColor: Color = Color.Black,
    fontSize: TextUnit = 20.sp,
    fontWeight: FontWeight = FontWeight.Bold,
    shadowOffset: Dp = 2.dp,
    borderWidth: Float = 4f,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val shadowOffsetPx = with(density) { shadowOffset.toPx() }

    Text(
        text = text,
        fontSize = fontSize,
        fontWeight = fontWeight,
        color = fillColor,
        style = TextStyle(
            shadow = Shadow(
                color = shadowColor,
                offset = Offset(shadowOffsetPx, shadowOffsetPx),
                blurRadius = 5f
            )
        ),
        modifier = modifier.drawBehind {
            val paint = Paint().asFrameworkPaint().apply {
                isAntiAlias = true
                style = android.graphics.Paint.Style.STROKE
                strokeWidth = borderWidth
                color = borderColor.toArgb()
                textSize = fontSize.toPx()
                typeface = android.graphics.Typeface.DEFAULT_BOLD
            }

            drawIntoCanvas { canvas ->
                canvas.nativeCanvas.drawText(
                    text,
                    0f,
                    fontSize.toPx(), // baseline
                    paint
                )
            }
        }
    )
}

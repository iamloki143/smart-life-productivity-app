package com.example.smartlife.ui.screen.home

import android.graphics.fonts.FontFamily
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.nativeCanvas

@Composable
fun AnimatedStrokeText(
    text: String,
    typeface: android.graphics.Typeface,
    animationKey: Int,
    modifier: Modifier = Modifier
) {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        progress.snapTo(0f)
        progress.animateTo(
            1f,
            animationSpec = tween(2000)
        )
    }

    androidx.compose.foundation.Canvas(modifier = modifier) {
        val paint = android.graphics.Paint().apply {
            isAntiAlias = true
            textSize = 150f
            style = android.graphics.Paint.Style.STROKE
            strokeWidth = 4f
            this.typeface=typeface
        }

        val path = android.graphics.Path()
        val textWidth = paint.measureText(text)
        val x = (size.width - textWidth) / 2

        val bounds = android.graphics.Rect()
        paint.getTextBounds(text, 0, text.length, bounds)

        val y = (size.height / 2f) + (bounds.height() / 2f)

        paint.getTextPath(text, 0, text.length, x, y, path)

        val measure = android.graphics.PathMeasure(path, false)
        val segment = android.graphics.Path()

        do {
            val length = measure.length
            val stop = length * progress.value

            measure.getSegment(0f, stop, segment, true)

        } while (measure.nextContour())

        drawContext.canvas.nativeCanvas.drawPath(segment, paint)
    }
}
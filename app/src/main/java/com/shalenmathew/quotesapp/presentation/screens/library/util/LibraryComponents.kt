package com.shalenmathew.quotesapp.presentation.screens.library.util

import androidx.compose.animation.core.EaseOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp

@Composable
fun WhiteCancelIcon(onClick: () -> Unit) {

    val focusManager = LocalFocusManager.current
    val hapticFeedback = LocalHapticFeedback.current

    IconButton(onClick = {
        // Trigger haptic feedback when cancel icon is clicked
        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        focusManager.clearFocus(true)
        onClick()
    }) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Cancel",
            tint = White
        )
    }
}

fun Modifier.animatedBorder(
    provideProgress: () -> Float,
    colorFocused: Color,
    colorUnfocused: Color
) = this.drawWithCache {
    val width = size.width
    val height = size.height

    val shape = CircleShape

    // Only works with RoundedCornerShape...
    val outline = shape.createOutline(size, layoutDirection, this) as Outline.Rounded

    // ... correction: Only works with same corner sizes everywhere
    val radius = outline.roundRect.topLeftCornerRadius.x
    val diameter = 2 * radius

    // Clockwise path
    val pathCw = Path()

    // Start top center
    pathCw.moveTo(width / 2, 0f)

    // Line to right
    pathCw.lineTo(width - radius, 0f)

    // Top right corner
    pathCw.arcTo(Rect(width - diameter, 0f, width, diameter), -90f, 90f, false)

    // Right edge
    pathCw.lineTo(width, height - radius)

    // Bottom right corner
    pathCw.arcTo(Rect(width - diameter, height - diameter, width, height), 0f, 90f, false)

    // Line to bottom center
    pathCw.lineTo(width / 2, height)

    // As above, but mirrored horizontally
    val pathCcw = Path()
    pathCcw.moveTo(width / 2, 0f)
    pathCcw.lineTo(radius, 0f)
    pathCcw.arcTo(Rect(0f, 0f, diameter, diameter), -90f, -90f, false)
    pathCcw.lineTo(0f, height - radius)
    pathCcw.arcTo(Rect(0f, height - diameter, diameter, height), 180f, -90f, false)
    pathCcw.lineTo(width / 2, height)

    val pmCw = PathMeasure().apply {
        setPath(pathCw, false)
    }
    val pmCcw = PathMeasure().apply {
        setPath(pathCcw, false)
    }

    fun DrawScope.drawIndicator(progress: Float, pathMeasure: PathMeasure) {
        val subPath = Path()
        pathMeasure.getSegment(0f, pathMeasure.length * EaseOut.transform(progress), subPath)
        drawPath(subPath, colorFocused, style = Stroke(3.dp.toPx(), cap = StrokeCap.Round))
    }

    onDrawBehind {
        // Draw the shape
        drawOutline(outline, colorUnfocused, style = Stroke(2.dp.toPx()))

        // Draw the indicators
        drawIndicator(provideProgress(), pmCw)
        drawIndicator(provideProgress(), pmCcw)
    }
}

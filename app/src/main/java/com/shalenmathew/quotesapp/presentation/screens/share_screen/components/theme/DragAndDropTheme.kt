package com.shalenmathew.quotesapp.presentation.screens.share_screen.components.theme

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.shalenmathew.quotesapp.R
import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.presentation.theme.cormorantItalic
import kotlin.math.roundToInt

@Composable
fun DragAndDropTheme(
    modifier: Modifier = Modifier,
    quote: Quote,
    imageUri: Uri? = null
) {
    var offset by remember { mutableStateOf(Offset.Zero) }
    var scale by remember { mutableStateOf(1f) }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        AsyncImage(
            model = imageUri ?: R.drawable.ic_mog,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.28f))
        )

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset {
                    IntOffset(
                        x = offset.x.roundToInt(),
                        y = offset.y.roundToInt()
                    )
                }
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale
                )
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        offset += pan
                        scale = (scale * zoom).coerceIn(0.5f, 5f)
                    }
                }
                .background(Color.Transparent, RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp, vertical = 100.dp)
        ) {
            Text(
                text = quote.quote,
                color = Color.Yellow,
                fontSize = 24.sp,
                lineHeight = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontFamily = cormorantItalic
            )
        }
    }
}

package com.shalenmathew.quotesapp.presentation.screens.share_screen.components

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Picture
import android.graphics.Rect
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.graphics.createBitmap
import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.presentation.screens.share_screen.QuoteStyle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume


/** older way to capture bitmap */
//
//@Composable
//fun CaptureBitmap(quoteData: Quote, quoteStyleState: QuoteStyle, onCapture: (ImageBitmap) -> Unit)
//{
//
//    val coroutineScope = rememberCoroutineScope()
//    var capturedImg by remember { mutableStateOf<ImageBitmap?>(null) }
////    val picture = remember { Picture() }
//
//    val picture = remember(quoteStyleState) { Picture() }
//    var isRecording by remember { mutableStateOf(false) }
//
//    LaunchedEffect(quoteStyleState){
//        coroutineScope.launch {
//            try {
//                capturedImg = createBitmapFromPicture(picture).asImageBitmap()
//                capturedImg?.let {
//                    onCapture(it)
//                }
//            } catch (e: Exception) {
//                // Handle any bitmap creation errors
//                e.printStackTrace()
//            }
//        }
//    }
//
//    val modifier = Modifier.drawWithCache{
//        val width = this.size.width
//        val height = this.size.height
//
//        onDrawWithContent {
//
//            try {
//
//                if (isRecording) {
//                    picture.endRecording()
//                    isRecording = false
//                }
//
//
//                val pictureCanvas = Canvas(
//                    picture.beginRecording(width.toInt(), height.toInt())
//                )
//                isRecording = true
//
//                draw(this, this.layoutDirection, pictureCanvas, this.size) {
//                    this@onDrawWithContent.drawContent()
//                }
//
//                picture.endRecording()
//                isRecording = false
//
//                drawIntoCanvas { canvas ->
//                    canvas.nativeCanvas.drawPicture(picture)
//                }
//            } catch (e: IllegalStateException) {
//                //  Handle Picture errors
//                e.printStackTrace()
//                isRecording = false
//                this@onDrawWithContent.drawContent()
//            }
//
//        }
//
//    }
//
//
//    when(quoteStyleState){
//        is QuoteStyle.DefaultTheme->{
//            DefaultQuoteCard(modifier = modifier,quoteData)
//        }
//        QuoteStyle.CodeSnippetTheme -> {
//            CodeSnippetStyleQuoteCard(modifier = modifier,quoteData)
//        }
////        QuoteStyle.SpotifyTheme -> {
////            SolidColorQuoteCard(modifier = modifier,quoteData)
////        }
//        QuoteStyle.bratTheme -> {
//            BratScreen(modifier = modifier,quoteData)
//        }
//
//        QuoteStyle.igorTheme -> {
//            IgorScreen(modifier = modifier,quoteData)
//        }
//
//        QuoteStyle.LiquidGlassTheme -> {
//            LiquidGlassScreen(modifier = modifier,quoteData)
//        }
//    }
//
//}
//
//
//private fun createBitmapFromPicture(picture: Picture): Bitmap
//{
//
//    val bitmap = createBitmap(picture.width, picture.height, Bitmap.Config.ARGB_8888)
//
//    val canvas = android.graphics.Canvas(bitmap)
//    canvas.drawColor(android.graphics.Color.TRANSPARENT, android.graphics.PorterDuff.Mode.CLEAR)
//    canvas.drawPicture(picture)
//
//    return bitmap
//}


@Composable
fun CaptureBitmap(
    quoteData: Quote,
    quoteStyleState: QuoteStyle,
    triggerCapture: Boolean,
    onCapture: (ImageBitmap) -> Unit
) {
    val context = LocalContext.current
    val view = LocalView.current
    val coroutineScope = rememberCoroutineScope()
    var capturedImg by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(quoteStyleState) {

        coroutineScope.launch(Dispatchers.Main) {
            try {

                if (triggerCapture) {

                    delay(50)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    Box(
        modifier = Modifier.onGloballyPositioned { coordinates ->

            if (triggerCapture) {
                coroutineScope.launch(Dispatchers.Main) {
                    try {
                        val bounds = coordinates.boundsInRoot()
                        val bitmap = captureView(
                            context as Activity,
                            view,
                            bounds.left.toInt(),
                            bounds.top.toInt(),
                            bounds.width.toInt(),
                            bounds.height.toInt()
                        )

                        bitmap?.let {
                            onCapture(it.asImageBitmap())
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    ) {
        when(quoteStyleState) {
            is QuoteStyle.DefaultTheme -> {
                DefaultQuoteCard(modifier = Modifier, quoteData)
            }
            QuoteStyle.CodeSnippetTheme -> {
                CodeSnippetStyleQuoteCard(modifier = Modifier, quoteData)
            }
            QuoteStyle.bratTheme -> {
                BratScreen(modifier = Modifier, quoteData)
            }
            QuoteStyle.igorTheme -> {
                IgorScreen(modifier = Modifier, quoteData)
            }
            QuoteStyle.LiquidGlassTheme -> {
                LiquidGlassScreen(modifier = Modifier, quoteData)
            }

            QuoteStyle.ReminderTheme -> {
                ReminderStyle(modifier = Modifier, quoteData)
            }
        }
    }
}




private suspend fun captureView(
    activity: Activity,
    view: android.view.View,
    left: Int,
    top: Int,
    width: Int,
    height: Int
): Bitmap? = suspendCancellableCoroutine { continuation ->

    try {
        if (width <= 0 || height <= 0) {
            continuation.resume(null)
            return@suspendCancellableCoroutine
        }

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val locationOnScreen = IntArray(2)
            view.getLocationOnScreen(locationOnScreen)

            val rect = Rect(
                locationOnScreen[0] + left,
                locationOnScreen[1] + top,
                locationOnScreen[0] + left + width,
                locationOnScreen[1] + top + height
            )

            PixelCopy.request(
                activity.window,
                rect,
                bitmap,
                { result ->
                    if (result == PixelCopy.SUCCESS) {
                        continuation.resume(bitmap)
                    } else {

                        val fallbackBitmap = fallbackCapture(view, left, top, width, height)
                        continuation.resume(fallbackBitmap)
                    }
                },
                Handler(Looper.getMainLooper())
            )
        } else {
            val fallbackBitmap = fallbackCapture(view, left, top, width, height)
            continuation.resume(fallbackBitmap)
        }

    } catch (e: Exception) {
        e.printStackTrace()
        continuation.resume(null)
    }
}

private fun fallbackCapture(
    view: android.view.View,
    left: Int,
    top: Int,
    width: Int,
    height: Int
): Bitmap? {
    return try {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)


        canvas.translate(-left.toFloat(), -top.toFloat())
        view.draw(canvas)

        bitmap
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}



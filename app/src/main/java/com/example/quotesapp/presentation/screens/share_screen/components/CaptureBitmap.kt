package com.example.quotesapp.presentation.screens.share_screen.components

import android.graphics.Bitmap
import android.graphics.Picture
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
import androidx.core.graphics.createBitmap
import com.example.quotesapp.domain.model.Quote
import com.example.quotesapp.presentation.screens.share_screen.QuoteStyle
import kotlinx.coroutines.launch


@Composable
fun CaptureBitmap(quoteData: Quote, quoteStyleState: QuoteStyle, onCapture: (ImageBitmap) -> Unit)
{

    val coroutineScope = rememberCoroutineScope()
    var capturedImg by remember { mutableStateOf<ImageBitmap?>(null) }
    val picture = remember { Picture() }

    LaunchedEffect(quoteStyleState){
        coroutineScope.launch {
            capturedImg = createBitmapFromPicture(picture).asImageBitmap()
            capturedImg?.let { it->
                onCapture(it)
            }
        }
    }

    val modifier = Modifier.drawWithCache{
        val width = this.size.width
        val height = this.size.height

        onDrawWithContent {

            val pictureCanvas = Canvas(picture.beginRecording(width.toInt(),height.toInt()))

            draw(this, this.layoutDirection, pictureCanvas, this.size) {
                this@onDrawWithContent.drawContent()
            }

            picture.endRecording()

            drawIntoCanvas { canvas -> canvas.nativeCanvas.drawPicture(picture) }

        }

    }


    when(quoteStyleState){
        is QuoteStyle.DefaultTheme->{
            DefaultQuoteCard(modifier = modifier,quoteData)
        }
        QuoteStyle.CodeSnippetTheme -> {
            CodeSnippetStyleQuoteCard(modifier = modifier,quoteData)
        }
        QuoteStyle.SpotifyTheme -> {
            SolidColorQuoteCard(modifier = modifier,quoteData)
        }
        QuoteStyle.bratTheme -> {
            BratScreen(modifier = modifier,quoteData)
        }
    }

}


private fun createBitmapFromPicture(picture: Picture): Bitmap{

    val bitmap = createBitmap(picture.width, picture.height, Bitmap.Config.ARGB_8888)

    val canvas = android.graphics.Canvas(bitmap)
    canvas.drawColor(android.graphics.Color.TRANSPARENT, android.graphics.PorterDuff.Mode.CLEAR)
    canvas.drawPicture(picture)

    return bitmap
}
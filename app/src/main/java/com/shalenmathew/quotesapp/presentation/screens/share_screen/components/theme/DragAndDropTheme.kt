package com.shalenmathew.quotesapp.presentation.screens.share_screen.components.theme

import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shalenmathew.quotesapp.R
import com.shalenmathew.quotesapp.domain.model.Quote

@Suppress("DEPRECATION")
@Composable
fun DragAndDropTheme(
    modifier: Modifier = Modifier,
    quote: Quote,
    imageUri: Uri? = null
) {
    val context = LocalContext.current
    val defaultImage = painterResource(R.drawable.sample_travel)

    val painter: Painter = remember(imageUri) {
        if (imageUri != null) {
            val bitmap = try {
                MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
            } catch (_: Exception) {
                null
            }
            if (bitmap != null) {
                BitmapPainter(bitmap.asImageBitmap())
            } else {
                defaultImage
            }
        } else {
            defaultImage
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )


        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp)
                .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Text(
                text = quote.quote,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

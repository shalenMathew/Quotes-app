package com.shalenmathew.quotesapp.presentation.screens.share_screen.components.theme

import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shalenmathew.quotesapp.R
import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.presentation.theme.Grey
import com.shalenmathew.quotesapp.presentation.theme.RobotoFont

/** Dice Dreams STYLE */
@Composable
fun DiceDreamsStyleQuoteCard(
    modifier: Modifier,
    quote: Quote = Quote(quote = "The man who moves a mountain begins by carrying away small stones", author = "Unknown", liked = true),
    color: Color = Grey,
    imageUri: Uri? = null,
    onPickImage: () -> Unit = {}
) {
    val context = LocalContext.current
    val image = painterResource(R.drawable.sample_dice_dream_2)

    val painter: Painter = remember(imageUri) {
        if (imageUri != null) {
            val bitmap = try {
                MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
            } catch (e: Exception) {
                null
            }
            if (bitmap != null) {
                BitmapPainter(bitmap.asImageBitmap())
            } else {
                image
            }
        } else {
            image
        }
    }

    Box (
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = color)
            .padding(5.dp),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth(.8f)
                .wrapContentHeight()
                .shadow(elevation = 20.dp, shape = RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
        ) {
            Image(
                painter = painter,
                contentDescription = "Dice Dreams",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Text(
                text = quote.quote,
                fontSize = 22.sp,
                fontFamily = RobotoFont,
                fontWeight = FontWeight.Medium,
                lineHeight = 30.sp,
                color = color,
                modifier = Modifier.padding(20.dp)
            )
        }
    }
}
package com.shalenmathew.quotesapp.presentation.screens.share_screen.components.theme

import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shalenmathew.quotesapp.R
import com.shalenmathew.quotesapp.domain.model.Quote



/** TRAVEL CARD THEME */
@Suppress("DEPRECATION")
@Composable
fun TravelCardTheme(
    modifier: Modifier = Modifier,
    quote: Quote = Quote(
        quote = "Pausing for a moment to look to inspiring leaders",
        author = "Unknown",
        liked = true
    ),
    imageUri: Uri? = null,
    onPickImage: () -> Unit = {}
) {
    val context = LocalContext.current
    val image = painterResource(R.drawable.sample_travel)

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
                .fillMaxWidth(0.84f)
                .wrapContentHeight()
                .border(8.dp, Color.White, RoundedCornerShape(32.dp))
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.Transparent)
            ) {
                // Transparent clickable top
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 220.dp)
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onPickImage() }
                        .background(Color.Transparent)
                )

                // White bottom part
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 20.dp)
                ) {
                    Column {
                        Text(
                            text = quote.quote,
                            color = Color(0xFF164036),
                            fontFamily = FontFamily(Font(R.font.glaciaiindifference_regular)),
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Author capsule
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 8.dp, bottom = 8.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = Color(0xFFFFB6E1),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = quote.author,
                                    color = Color(0xFF5C5088),
                                    fontFamily = FontFamily(Font(R.font.glaciaiindifference_regular)),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
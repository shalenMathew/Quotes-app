package com.shalenmathew.quotesapp.presentation.screens.share_screen.components.theme

import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shalenmathew.quotesapp.R
import com.shalenmathew.quotesapp.domain.model.Quote

@Composable
fun PurpleDotTheme(
    modifier: Modifier = Modifier,
    quote: Quote = Quote(
        quote = "In publishing and graphic design, Lorem ipsum is a placeholder...",
        author = "Add a subheading",
        liked = true
    ),
    imageUri: Uri? = null,
    onPickImage: () -> Unit = {}
) {
    val context = LocalContext.current
    val fallbackImage = painterResource(R.drawable.sample_purple_dot_2)

    val painter: Painter = remember(imageUri, context) {
        if (imageUri != null) {
            val bitmap = try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    context.contentResolver.loadThumbnail(
                        imageUri,
                        android.util.Size(512, 512),
                        null
                    )
                } else {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(
                        context.contentResolver,
                        imageUri
                    )
                }
            } catch (_: Exception) {
                null
            }

            bitmap?.let { BitmapPainter(it.asImageBitmap()) }
                ?: fallbackImage
        } else {
            fallbackImage
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.Black)
            .padding(horizontal = 5.dp, vertical = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFD8A0E7))
                .padding(horizontal = 12.dp, vertical = 15.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    val headingText = if (quote.author.isNotBlank()) {
                        quote.author
                    } else {
                        "Unknown"
                    }

                    Text(
                        text = headingText,
                        color = Color(0xFF171717),
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 22.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )

                    val subtextText = quote.quote.ifBlank {
                        "In publishing and graphic design, Lorem ipsum is a placeholder..."
                    }

                    Text(
                        text = subtextText,
                        color = Color(0xFF171717),
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 18.sp,
                            fontWeight = FontWeight.Normal,
                            fontStyle = FontStyle.Normal
                        ),
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF3A323D))
                        .border(
                            width = 3.dp,
                            color = Color(0xFF3A323D),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(4.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onPickImage() }
                ) {
                    Image(
                        painter = painter,
                        contentDescription = "quote image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

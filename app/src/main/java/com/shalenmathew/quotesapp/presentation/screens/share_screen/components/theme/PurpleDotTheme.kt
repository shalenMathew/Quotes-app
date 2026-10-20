package com.shalenmathew.quotesapp.presentation.screens.share_screen.components.theme

import android.net.Uri
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
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
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.Black)
            .padding(horizontal = 20.dp, vertical = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(22.dp))
                .background(Color(0xFFD8A0E7))
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    val headingText = quote.author.ifBlank { "Add a subheading" }

                    Text(
                        text = headingText,
                        color = Color(0xFF171717),
                        style = TextStyle(
                            fontSize = 15.sp,
                            lineHeight = 20.sp,
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
                            fontSize = 13.sp,
                            lineHeight = 17.sp,
                            fontWeight = FontWeight.Normal,
                            fontStyle = FontStyle.Normal
                        ),
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(105.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0xFF3A323D))
                        .border(
                            width = 3.dp,
                            color = Color(0xFF3A323D),
                            shape = RoundedCornerShape(18.dp)
                        )
                        .padding(3.dp)
                        .clip(RoundedCornerShape(13.dp))
                        .clickable { onPickImage() }
                ) {
                    AsyncImage(
                        model = imageUri ?: R.drawable.sample_purple_dot_2,
                        contentDescription = "quote image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

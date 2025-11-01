package com.shalenmathew.quotesapp.presentation.screens.share_screen.components.theme

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.shalenmathew.quotesapp.R
import com.shalenmathew.quotesapp.domain.model.Quote


/** YOUTUBE STYLE THEME */
@Preview
@Composable
fun YoutubeStyleTheme(
    modifier: Modifier = Modifier,
    quote: Quote = Quote(
        quote = "Pausing for a moment to look to inspiring leaders",
        author = "Unknown",
        liked = true
    ),
    thumbnailUri: Uri? = null,
    onPickImage: (() -> Unit)? = null
) {
    val context = LocalContext.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color(0xFF0F0F0F))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            // Thumbnail Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clickable { onPickImage?.invoke() }
            ) {
                if (thumbnailUri != null) {
                    AsyncImage(
                        model = thumbnailUri,
                        contentDescription = "Video thumbnail",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Default placeholder
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Image(
                            painter = painterResource(R.drawable.thumbnaildefault),
                            contentDescription = "Placeholder",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                // Duration overlay (bottom right)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .background(
                            Color.Black.copy(alpha = 0.8f),
                            RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "20:25",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Video metadata section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 12.dp)
            ) {
                // Channel logo / App logo (circular)
                Image(
                    painter = painterResource(R.drawable.quotes_logo),
                    contentDescription = "Channel logo",
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Title, channel, views, date column
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight()
                ) {
                    // Video title (Quote)
                    Text(
                        text = quote.quote,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 20.sp,
                        maxLines = 6,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp)
                    )

                    Row(
                        modifier = Modifier
                            .padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Channel name (app)
                        Text(
                            text = context.getString(R.string.app_name),
                            color = Color(0xFFAAAAAA),
                            fontSize = 14.sp,
                            modifier = Modifier.padding(end = 4.dp)
                        )

                        Text(
                            text = " · ",
                            color = Color(0xFFAAAAAA),
                            fontSize = 14.sp
                        )

                        // Views and date (placeholder)
                        Text(
                            text = "13K views · 7 days ago",
                            color = Color(0xFFAAAAAA),
                            fontSize = 14.sp
                        )
                    }
                }

                // Three dots menu
                IconButton(
                    onClick = { },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Menu",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

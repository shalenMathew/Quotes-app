package com.shalenmathew.quotesapp.presentation.screens.share_screen.components.theme

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.R


@Composable
fun TwitterStyleTheme(
    modifier: Modifier = Modifier,
    quote: Quote,
    twitterProfileUri: Uri? = null
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight() // Makes the overall background tall like the image
            .background(Color(0xFFE1E1E1))
            .padding(horizontal = 24.dp, vertical = 40.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                // Removing fillMaxHeight here ensures the white card only
                // takes up the space it needs for the text
                .shadow(8.dp, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .padding(horizontal = 24.dp, vertical = 120.dp) // Increased vertical padding for that "airy" look
        ) {
            // Header: Profile Pic, Username, and Verified Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = twitterProfileUri ?: R.drawable.quotes_logo,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.LightGray, CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = quote.author.lowercase().replace(" ", ""),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                )

                Spacer(modifier = Modifier.width(4.dp))

                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "Verified",
                    tint = Color(0xFF3F85F3),
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp)) // Increased spacing

            // The Quote
            Text(
                text = quote.quote,
                style = TextStyle(
                    fontSize = 19.sp, // Slightly larger to match the image impact
                    lineHeight = 28.sp,
                    color = Color.Black,
                    fontFamily = FontFamily.SansSerif
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // Optional: Bottom handle matching the image footer
            Text(
                text = "@${quote.author.lowercase().replace(" ", "")}",
                modifier = Modifier.padding(top = 40.dp), // Pushes the handle further down
                style = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}
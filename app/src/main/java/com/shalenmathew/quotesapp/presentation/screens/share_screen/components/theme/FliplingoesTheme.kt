package com.shalenmathew.quotesapp.presentation.screens.share_screen.components.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.presentation.theme.GIFont

@Composable
fun FliplingoesTheme(
    quote: Quote,
    modifier: Modifier = Modifier
) {
    val backgroundColor = Color(0xFF4C450C)
    val cardColor = Color(0xFFFFEB3B)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .padding(40.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(cardColor)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = quote.quote,
                fontFamily = GIFont,
                color = Color.Black,
                fontSize = 25.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 30.sp,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(bottom = 100.dp, end = 20.dp)
            )

            if (!quote.author.isNullOrBlank()) {
                Text(
                    text = quote.author,
                    fontFamily = GIFont,
                    color = Color.Black,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.BottomStart)
                )
            }
        }
    }
}
package com.shalenmathew.quotesapp.presentation.screens.share_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.presentation.theme.GIFont
import hilt_aggregated_deps._com_shalenmathew_quotesapp_QuoteApplication_GeneratedInjector
import kotlin.text.Typography.quote

@Composable
fun FliplingoesPreviewCard(modifier: Modifier = Modifier,quote : Quote) {
    val backgroundColor = Color(0xFF534D09)
    val cardColor = Color(0xFFFFEB3B)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(backgroundColor)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(cardColor)
                .padding(16.dp)
        ) {
            Text(
                text = quote.quote,
                fontFamily = GIFont,
                color = Color.Black,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FliplingoesPreviewCardPreview() {
    val sampleQuote = Quote(
        quote = "The future belongs to those who believe in the beauty of their dreams.",
        author = "Eleanor Roosevelt",
        liked = false
    )

    FliplingoesPreviewCard(modifier = Modifier, quote = sampleQuote)
}



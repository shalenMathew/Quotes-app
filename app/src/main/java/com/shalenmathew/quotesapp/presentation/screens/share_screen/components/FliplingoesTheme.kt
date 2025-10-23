// File: com.shalenmathew.quotesapp.presentation.screens.share_screen.components/FliplingoesTheme.kt

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.presentation.theme.GIFont

@Composable
fun FliplingoesTheme(
    quote: Quote,
    modifier: Modifier = Modifier
) {
    // Background color (Keeping the improved color from the previous answer, or use the original 0xFF534D09)
    val backgroundColor = Color(0xFF4C450C)
    val cardColor = Color(0xFFFFEB3B)


    val cardWidth = 280.dp
    val cardHeight = 350.dp // Making the height significantly larger than the width

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(cardWidth, cardHeight)
                .clip(RoundedCornerShape(12.dp))
                .background(cardColor)
                .padding(24.dp), // Padding inside the yellow card
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,

                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                // The quote text will now be centered vertically and horizontally in the fixed size box.
                Text(
                    text = quote.quote,
                    fontFamily = GIFont,
                    color = Color.Black,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Medium
                )

                // Keep author if it exists (but the original Fliplingoes card doesn't have it)
                if (!quote.author.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = quote.author,
                        fontFamily = GIFont,
                        color = Color.Black,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
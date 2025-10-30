package com.shalenmathew.quotesapp.presentation.screens.share_screen.components.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shalenmathew.quotesapp.domain.model.Quote

@Composable
fun MinimalBlackTheme(
    quote: Quote,
    modifier: Modifier = Modifier
) {
    val backgroundColor = Color.Black
    val textColor = Color.White

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(horizontal = 50.dp, vertical = 80.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = quote.quote.lowercase(),
            color = textColor,
            fontSize = 42.sp,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Bold,
            lineHeight = 52.sp,
            modifier = Modifier.align(Alignment.CenterStart)
        )
    }
}

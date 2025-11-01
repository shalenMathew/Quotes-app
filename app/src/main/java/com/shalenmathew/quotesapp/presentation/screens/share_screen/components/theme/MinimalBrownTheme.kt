package com.shalenmathew.quotesapp.presentation.screens.share_screen.components.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shalenmathew.quotesapp.domain.model.Quote



/** MINIMAl BROWN THEME */
@Preview
@Composable
fun MinimalBrownTheme(
    modifier: Modifier = Modifier,
    quote: Quote = Quote(
        quote = "Pausing for a moment to look to inspiring leaders",
        author = "Unknown",
        liked = true
    )
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color(0xFF7D5B43))
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(horizontal = 20.dp, vertical = 180.dp)
                .wrapContentHeight(),
        ) {
            Text(
                text = quote.quote,
                color = Color(0xFFEDE1D6),
                fontSize = 20.sp,
                lineHeight = 30.sp,
                textAlign = TextAlign.Start
            )
        }
    }
}
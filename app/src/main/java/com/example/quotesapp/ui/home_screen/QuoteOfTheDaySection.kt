package com.example.quotesapp.ui.home_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quotesapp.ui.theme.GIFont
import com.example.quotesapp.ui.viewmodel.QuoteViewModel

@Composable
fun QuoteOfTheDaySection(quoteViewModel: QuoteViewModel) {

    val state = quoteViewModel.quoteState.value

    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .background(Color.Transparent)){

        Column(modifier = Modifier.wrapContentHeight().fillMaxWidth()) {
            Text(text = "Quote of the day",
                fontFamily = GIFont,
                fontSize = 35.sp,
                modifier = Modifier.padding(start = 15.dp),
                color = Color.White)

            Text(text = state.qot?.quote ?: state.error,
                fontFamily = GIFont,
                fontWeight = FontWeight.Thin,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 15.dp, top = 5.dp, end = 15.dp),
                color = Color.White)
        }
    }
}
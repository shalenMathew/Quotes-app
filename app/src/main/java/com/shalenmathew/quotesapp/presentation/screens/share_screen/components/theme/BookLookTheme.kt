package com.shalenmathew.quotesapp.presentation.screens.share_screen.components.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.R


@Preview
@Composable
fun BookLookTheme(
    modifier: Modifier = Modifier,
    quote: Quote = Quote(
        quote = "Pausing for a moment to look to inspiring leaders",
        author = "Unknown",
        liked = true
    )
){


    Box(
        modifier = modifier.wrapContentSize()
    ) {


        Image(
            painter = painterResource(id = R.drawable.theme_paper_bg),
            contentDescription = null,
            modifier = Modifier.wrapContentSize(),
            contentScale = ContentScale.Fit

        )


        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(horizontal = 48.dp, vertical = 0.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = quote.quote,
                color = Color.Black,
                fontSize = 18.sp,
                lineHeight = 24.sp,
                textAlign = TextAlign.Start,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Normal
            )

        }
    }

}
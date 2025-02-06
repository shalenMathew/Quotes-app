package com.example.quotesapp.ui

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.quotesapp.R
import com.example.quotesapp.domain.model.Quote
import com.example.quotesapp.ui.home_screen.util.QuoteEvent
import com.example.quotesapp.ui.theme.GIFont
import com.example.quotesapp.ui.theme.customBlack
import com.example.quotesapp.ui.theme.customGrey


@Preview
@Composable
fun QuoteItem(data:Quote=Quote(quote = "ok", author = "o", liked = false)){

    val context = LocalContext.current


    val gradient = Brush.radialGradient(
        0.0f to customBlack,
        1.0f to customGrey,
        radius = 1000.0f,
        tileMode = TileMode.Repeated
    )

    Box(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(gradient)
            .wrapContentHeight()){

        AsyncImage(
            model = R.drawable.quotation,
            contentDescription = null,
            modifier= Modifier
                .align(Alignment.TopStart)
                .padding(start=12.dp,top = 15.dp)
                .size(30.dp))

        Column(modifier = Modifier.wrapContentSize()
            .background(Color.Transparent)
            .align(Alignment.Center)
        ) {
            Text(text = data.quote,
                fontFamily = GIFont,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 15.dp)
                    .fillMaxWidth(),
                color = White,
                style = TextStyle(
                    lineHeight = 40.sp // Set the line height
                )
            )

            Spacer(modifier= Modifier.height(50.dp))

            Text(text = data.author,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 15.dp))
        }

    }
}
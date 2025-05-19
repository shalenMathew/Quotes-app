package com.shalenmathew.quotesapp.presentation.screens.share_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.presentation.theme.DarkerGrey
import com.shalenmathew.quotesapp.presentation.theme.Green
import com.shalenmathew.quotesapp.presentation.theme.Poppins
import com.shalenmathew.quotesapp.presentation.theme.Violet
import com.shalenmathew.quotesapp.presentation.theme.bratGreen
import com.shalenmathew.quotesapp.presentation.theme.bratTheme
import com.shalenmathew.quotesapp.R
import com.shalenmathew.quotesapp.presentation.theme.handWritten
import com.shalenmathew.quotesapp.presentation.theme.sugarPie

/**  THIS SECTION COMPRISES OF ALL DIFFERENT STYLES OF QUOTES */

 /** DEFAULT STYLE */
//@Preview
@Composable
fun DefaultQuoteCard(modifier: Modifier, quote: Quote) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(Color.Black, Color(0xFF383838)),
                        center = Offset.Unspecified,
                        radius = 1000f
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            Image(
                painter = painterResource(id = R.drawable.quotation),
                contentDescription = null,
                modifier = Modifier
                    .padding( horizontal = 15.dp,vertical = 20.dp)
                    .size(25.dp)
                    .align(Alignment.TopStart),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 80.dp)
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = quote.quote,
                    fontSize = 19.sp,
                    lineHeight = 38.sp,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.glaciaiindifference_regular)),
                    modifier = Modifier.padding(top = 12.dp).align(Alignment.Start)
                )

                Text(
                    text = quote.author,
                    fontSize = 18.sp,
                    color = DarkerGrey,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 30.dp)
                )
            }

            Text(
                text = "Quotes.app",
                fontSize = 16.sp,
                lineHeight = 32.sp,
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.glaciaiindifference_regular)),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 5.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

/** CODE SNIPPET STYLE */
@Composable
fun CodeSnippetStyleQuoteCard(modifier: Modifier,quote: Quote) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Violet)
    ) {
        Card (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 50.dp)
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(8.dp),
                    ambientColor = Color.Black.copy(alpha = 1f),
                    spotColor = Color.Black.copy(alpha = 1f)
                )
            ,
            shape = RoundedCornerShape(5.dp),
            elevation = CardDefaults.cardElevation(5.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Black)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
//                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.padding(bottom = 28.dp, start = 20.dp, top = 24.dp)
                ) {
                    CircleDot(color = Color(0xFFFF3B30))
                    Spacer(modifier = Modifier.width(8.dp))
                    CircleDot(color = Color(0xFFFFCC00))
                    Spacer(modifier = Modifier.width(8.dp))
                    CircleDot(color = Color(0xFF4CD964))
                }

                Text(
                    text = quote.quote,
                    color = Color.White,
                    fontSize = 15.sp,
                    lineHeight = 30.sp,
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp)
                )

                Text(
                    text = quote.author,
                    color = Color(0xFF00E0FF),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp,)
                )

                Text(
                    text = "Quotes.app",
                    color = Color(0xFFFF48B0),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(top = 20.dp, bottom = 8.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
fun CircleDot(color: Color) {
    Box(
        modifier = Modifier
            .size(12.dp)
            .background(color = color, shape = CircleShape)
    )
}

/** SPOTIFY THEME STYLE */
@Composable
fun SolidColorQuoteCard(modifier: Modifier,quote: Quote) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Green)

    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = quote.author,
                fontSize = 18.sp,
                color = Color.Black,
                fontFamily = Poppins,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 15.dp, top = 60.dp,start = 18.dp, end = 18.dp)
            )

            Text(
                text = quote.quote,
                fontSize = 18.sp,
                color = Color.Black,
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                lineHeight = 30.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = 30.dp, start = 18.dp, end = 18.dp)
            )

            Text(
                text = "Quotes.app",
                color =Color.Black,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(top = 30.dp, bottom = 12.dp)
                    .align(Alignment.CenterHorizontally)
            )

        }

    }
}


/** brat THEME STYLE */
@Composable
fun BratScreen(modifier: Modifier,quote: Quote) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = bratGreen)
            .padding(5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = quote.quote,
            fontSize = 18.sp,
            fontFamily = bratTheme,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    }
}



/** IGOR THEME STYLE */
@Composable
fun IgorScreen(modifier: Modifier, quote: Quote) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color(0xfff1aec3))
    ) {
        Image(
            painter = painterResource(id = R.drawable.igor_angels),
            contentDescription = "Cherubs",
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = (-45).dp, y = (-55).dp)
                .size(170.dp),
            contentScale = ContentScale.Crop
        )

        Image(
            painter = painterResource(id = R.drawable.igor_flowers),
            contentDescription = "Sunflowers",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (45).dp, y = (-60).dp)
                .size(170.dp),
            contentScale = ContentScale.Crop
        )

        Image(
            painter = painterResource(id = R.drawable.igor_crocodiles),
            contentDescription = "Crocodile",
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-45).dp, y = 50.dp)
                .size(180.dp),
            contentScale = ContentScale.Crop
        )

        Image(
            painter = painterResource(id = R.drawable.igor_statue),
            contentDescription = "Statue Collage",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 55.dp, y = 30.dp)
                .size(230.dp),
            contentScale = ContentScale.Fit
        )

        Text(
            text = quote.quote,
            style = TextStyle(
                fontSize = 24.sp,
                color = Color.Black,
                fontFamily = handWritten,
                fontWeight = FontWeight.Bold
            )
            ,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 15.dp, vertical = 230.dp)
        )
    }

}





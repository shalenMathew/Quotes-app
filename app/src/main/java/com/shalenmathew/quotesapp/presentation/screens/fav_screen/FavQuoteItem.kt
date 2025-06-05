package com.shalenmathew.quotesapp.presentation.screens.fav_screen

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.shalenmathew.quotesapp.R
import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.presentation.screens.fav_screen.util.FavQuoteEvent
import com.shalenmathew.quotesapp.presentation.screens.home_screen.bottom_nav.Screen
import com.shalenmathew.quotesapp.presentation.theme.GIFont
import com.shalenmathew.quotesapp.presentation.theme.customBlack
import com.shalenmathew.quotesapp.presentation.theme.customGrey
import com.shalenmathew.quotesapp.presentation.viewmodel.FavQuoteViewModel



@Composable
fun FavQuoteItem(quote: Quote,
                 quoteViewModel: FavQuoteViewModel,
                 navHost: NavHostController,
                 modifier: Modifier){

    val context = LocalContext.current
    val activity = context as ComponentActivity

    val gradient = Brush.radialGradient(
        0.0f to customBlack,
        1.0f to customGrey,
        radius = 600.0f,
        tileMode = TileMode.Repeated
    )

    Box(
        modifier = modifier
            .padding(horizontal = 12.dp, vertical = 5.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(gradient)
            .fillMaxSize()){

        Column(modifier = Modifier.wrapContentSize()) {

            AsyncImage(
                model = R.drawable.quotation,
                contentDescription = null,
                modifier= Modifier

                    .padding(start=12.dp,top = 10.dp)
                    .size(30.dp))

            Text(text = quote.quote,
                fontFamily = GIFont,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp),
                color = Color.White,
                style = TextStyle(
                    lineHeight = 40.sp // Set the line height
                ))

            Spacer(modifier= Modifier.height(20.dp))

            Text(text = quote.author,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 15.dp))



               Box(modifier = Modifier.wrapContentWidth()){

                   Row(modifier = Modifier.fillMaxWidth(),
                       horizontalArrangement = Arrangement.End)
                   {

                       AsyncImage(model = R.drawable.send,
                           contentDescription = null,
                           modifier= Modifier
                               .padding(end = 12.dp, bottom = 10.dp)
                               .size(35.dp)
                               .clickable {

                                   navHost.currentBackStackEntry?.savedStateHandle?.set("quote",quote)
                                   navHost.navigate(Screen.Share.route)

                           })

                       Spacer(modifier= Modifier.width(15.dp))

                       AsyncImage(model = R.drawable.heart_filled,
                           contentDescription = null,
                           modifier= Modifier
                               .padding(end = 12.dp, bottom = 10.dp)
                               .size(30.dp)
                               .clickable {
                                   quoteViewModel.onEvent(FavQuoteEvent.Like(quote))

                               })
                   }

            }

        }

    }
}
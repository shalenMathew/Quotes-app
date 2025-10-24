package com.shalenmathew.quotesapp.presentation.screens.home_screen

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.aghajari.compose.lazyswipecards.LazySwipeCards
import com.shalenmathew.quotesapp.R
import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.presentation.screens.bottom_nav.Screen
import com.shalenmathew.quotesapp.presentation.screens.home_screen.util.QuoteEvent
import com.shalenmathew.quotesapp.presentation.theme.GIFont
import com.shalenmathew.quotesapp.presentation.theme.customBlack
import com.shalenmathew.quotesapp.presentation.theme.customGrey
import com.shalenmathew.quotesapp.presentation.viewmodel.QuoteViewModel


@Composable
fun QuoteItem(data: Quote, quoteViewModel: QuoteViewModel, navHost: NavHostController){

    val context = LocalContext.current
    val activity = context as ComponentActivity
    
    // Observe the current state to get the updated quote with correct liked status
    val currentState = quoteViewModel.quoteState.value
    val currentQuote = currentState.dataList.find { it.id == data.id } ?: data

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
            .fillMaxSize()){


        Image(
            painter = painterResource(R.drawable.quotation),
            contentDescription = null,
            modifier= Modifier
                .align(Alignment.TopStart)
                .padding(start=12.dp,top = 15.dp)
                .size(30.dp))

        Column(modifier = Modifier.wrapContentSize()
            .background(Color.Transparent)
            .align(Alignment.Center)
        ) {
            Text(text = currentQuote.quote,
                fontFamily = GIFont,
                fontWeight = FontWeight.Normal,
                fontSize = 19.sp,
                modifier = Modifier.padding(horizontal = 15.dp)
                    .fillMaxWidth(),
                color = White,
                style = TextStyle(
                    lineHeight = 40.sp
                )
            )

            Spacer(modifier= Modifier.height(50.dp))

            Text(text = currentQuote.author,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 15.dp))
        }

        Column(modifier= Modifier.wrapContentSize()
            .align(Alignment.BottomEnd)
            .padding(horizontal = 20.dp,vertical=28.dp)) {


                if (currentQuote.liked )
                {

                    AsyncImage(model = R.drawable.heart_filled,
                        contentDescription = null,
                        modifier= Modifier.size(35.dp)
                            .clickable {
                                quoteViewModel.onEvent(QuoteEvent.Like(currentQuote))

                            })
                }
                else
                {
                    AsyncImage(model = R.drawable.heart_unfilled,
                        contentDescription = "share",
                        modifier= Modifier.size(35.dp)
                            .clickable {
                                quoteViewModel.onEvent(QuoteEvent.Like(currentQuote))
                            })
                }

            Spacer(modifier= Modifier.height(25.dp))

            AsyncImage(model = R.drawable.send,
                contentDescription = null,
                modifier= Modifier.size(35.dp).testTag("share").clickable
                {

                    navHost.currentBackStackEntry?.savedStateHandle?.set("quote",currentQuote)
                    navHost.navigate(Screen.Share.route)


                })
        }
    }
}


@Composable
fun QuoteItemListSection(
    quoteViewModel: QuoteViewModel,
    navHost: NavHostController,
) {

    val state = quoteViewModel.quoteState.value

    if(state.isLoading)
    {


        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Transparent)
            , contentAlignment = Alignment.Center){
            CircularProgressIndicator(color = White)
        }
    }
    else if (state.error.isNotEmpty())
    {
        Box(modifier = Modifier
            .padding(5.dp)
            .fillMaxSize()
            .background(color = Color.Transparent)
            , contentAlignment = Alignment.Center){

            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Text(text = state.error,color = White, modifier = Modifier.fillMaxWidth(),textAlign = TextAlign.Center)

                Spacer(modifier = Modifier.height(15.dp))

                Button(onClick = {

                    // retry fetching data
                    quoteViewModel.onEvent(QuoteEvent.Retry)

                }, colors = ButtonDefaults.buttonColors(White)) {
                    Text("Refresh",
                        color = Color.Black, fontSize = 15.sp,
                        modifier = Modifier.padding(5.dp))
                }
            }
        }

    }
    else
    {

            LazySwipeCards(cardColor = Color.Transparent,
                cardShadowElevation = 0.dp,
                translateSize = 8.dp,
                swipeThreshold = 0.3f) {

                items(state.dataList) {it->
                    QuoteItem(it,quoteViewModel,navHost)
                }
                onSwiped { item, _ ->
                    quoteViewModel.onEvent(QuoteEvent.Swipe(item as Quote))
                }
            }
    }
    }




package com.example.quotesapp.presentation.home_screen

import android.os.Bundle
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.aghajari.compose.lazyswipecards.LazySwipeCards
import com.example.quotesapp.QuoteApplication
import com.example.quotesapp.R
import com.example.quotesapp.domain.model.Quote
import com.example.quotesapp.presentation.home_screen.util.QuoteEvent
import com.example.quotesapp.presentation.theme.GIFont
import com.example.quotesapp.presentation.theme.customBlack
import com.example.quotesapp.presentation.theme.customGrey
import com.example.quotesapp.presentation.viewmodel.QuoteViewModel
import com.google.firebase.analytics.FirebaseAnalytics

@Composable
fun QuoteItem(data: Quote, quoteViewModel: QuoteViewModel){

    val context = LocalContext.current
    val activity = context as ComponentActivity
    val firebaseAnalytics = (activity.application as QuoteApplication).firebaseAnalytics


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
            Text(text = data.quote,
                fontFamily = GIFont,
                fontWeight = FontWeight.Normal,
                fontSize = 19.sp,
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

        Column(modifier= Modifier.wrapContentSize()
            .align(Alignment.BottomEnd)
            .padding(horizontal = 20.dp,vertical=28.dp)) {


                if (data.liked ){

                    AsyncImage(model = R.drawable.heart_filled,
                        contentDescription = null,
                        modifier= Modifier.size(35.dp)
                            .clickable {
                                quoteViewModel.onEvent(QuoteEvent.Like(data))

                                val bundle= Bundle().apply {
                                    putString(FirebaseAnalytics.Param.CONTENT_TYPE,"btn_click")
                                    putString(FirebaseAnalytics.Param.ITEM_NAME, "like")
                                }

                                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM,bundle)

                            })
                }else{
                    AsyncImage(model = R.drawable.heart_unfilled,
                        contentDescription = null,
                        modifier= Modifier.size(35.dp)
                            .clickable {
                                quoteViewModel.onEvent(QuoteEvent.Like(data))

                                val bundle= Bundle().apply {
                                    putString(FirebaseAnalytics.Param.CONTENT_TYPE,"btn_click")
                                    putString(FirebaseAnalytics.Param.ITEM_NAME, "unlike")
                                }

                                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM,bundle)

                            })
                }

            Spacer(modifier= Modifier.height(25.dp))

            AsyncImage(model = R.drawable.send,
                contentDescription = null,
                modifier= Modifier.size(35.dp).clickable {

                    createImageFromXml(context, data) { bitmap ->
                        showSharePreview(context,bitmap)
                    }

                    val bundle= Bundle().apply {
                        putString(FirebaseAnalytics.Param.CONTENT_TYPE,"btn_click")
                        putString(FirebaseAnalytics.Param.ITEM_NAME, "share")
                    }

                    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE,bundle)

                })
        }
    }
}


@Composable
fun QuoteItemListSection( quoteViewModel: QuoteViewModel) {

    val state = quoteViewModel.quoteState.value

    if(state.isLoading){
        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Transparent)
            , contentAlignment = Alignment.Center){
            CircularProgressIndicator(color = White)
        }
    }else if (state.error.isNotEmpty()){

        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Transparent)
            , contentAlignment = Alignment.Center){
              Text(state.error, color = White)
        }

    }else{
            LazySwipeCards(cardColor = Color.Transparent,
                cardShadowElevation = 0.dp,
                translateSize = 8.dp,
                swipeThreshold = 0.4f) {

                items(state.dataList) {it->
                    QuoteItem(it,quoteViewModel)
                }
                onSwiped { item, _ ->
                    state.dataList.add(item as Quote)
                }
            }
    }
    }


package com.example.quotesapp.ui.fav_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quotesapp.ui.viewmodel.FavQuoteViewModel


@Composable
fun FavScreen(paddingValues: PaddingValues, quoteViewModel:FavQuoteViewModel= hiltViewModel()) {

    Box(modifier=Modifier.padding(paddingValues)
        .fillMaxSize().background(color = Color.Black)){

        if(quoteViewModel.favQuoteState.value.isLoading){
            Box(modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Transparent)
                , contentAlignment = Alignment.Center){
                Text(quoteViewModel.favQuoteState.value.error, color = White)
            }
        }else{
            LazyColumn(modifier=Modifier.fillMaxSize()) {
                items(quoteViewModel.favQuoteState.value.dataList){ quote ->
                    FavQuoteItem(quote,quoteViewModel)
                }

            }
        }

    }

}
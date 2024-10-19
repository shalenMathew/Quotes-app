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
import com.example.quotesapp.ui.theme.GIFont
import com.example.quotesapp.ui.viewmodel.FavQuoteViewModel


@Composable
fun FavScreen(paddingValues: PaddingValues, quoteViewModel:FavQuoteViewModel= hiltViewModel()) {


    val state = quoteViewModel.favQuoteState.value

    Box(modifier=Modifier.padding(paddingValues)
        .fillMaxSize().background(color = Color.Black)){

            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.Transparent), contentAlignment = Alignment.Center
                ) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.Transparent)
                        , contentAlignment = Alignment.Center){
                        Text(state.error, color = White)
                    }
                }
            } else {
                if(state.dataList.isNotEmpty()){
                    LazyColumn(modifier=Modifier.fillMaxSize()) {
                        items(state.dataList) { quote ->
                            FavQuoteItem(quote, quoteViewModel)
                        }
                    }
                }else{

                    Text("Looks empty...",
                        color = White,
                        fontFamily = GIFont,
                        modifier = Modifier.align(
                        Alignment.Center))
                }
            }

        }

    }

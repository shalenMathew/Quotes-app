package com.example.quotesapp.presentation.home_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.quotesapp.R
import com.example.quotesapp.presentation.viewmodel.QuoteViewModel


@Composable
fun HomeScreen(paddingValues: PaddingValues,
               quoteViewModel: QuoteViewModel){

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)){
        Image(painter = painterResource(R.drawable.bg),
            contentDescription = null,
            modifier = Modifier.size(200.dp).align(Alignment.TopEnd))

        Column(modifier = Modifier.fillMaxSize()
            .background(Color.Transparent)
            .padding(paddingValues)) {

            QuoteOfTheDaySection(quoteViewModel)
            QuoteItemListSection(quoteViewModel)
        }
    }
}






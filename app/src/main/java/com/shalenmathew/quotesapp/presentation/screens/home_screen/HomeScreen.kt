package com.shalenmathew.quotesapp.presentation.screens.home_screen

import android.os.Build
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.shalenmathew.quotesapp.R
import com.shalenmathew.quotesapp.presentation.viewmodel.QuoteViewModel


@Composable
fun HomeScreen(paddingValues: PaddingValues,
               navHost: NavHostController,
               quoteViewModel: QuoteViewModel= hiltViewModel()
){

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)){

        val painter = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            rememberAsyncImagePainter(R.drawable.bg)
        } else {
            painterResource(R.drawable.bg)
        }

        Image(painter = painter,
            contentDescription = null,
            modifier = Modifier.size(200.dp).align(Alignment.TopEnd),

            )

        Column(modifier = Modifier.fillMaxSize()
            .background(Color.Transparent)
            .padding(paddingValues)) {

            QuoteOfTheDaySection(quoteViewModel)
            QuoteItemListSection(quoteViewModel,navHost)
        }
    }
}






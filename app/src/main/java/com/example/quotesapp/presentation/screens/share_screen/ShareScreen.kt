package com.example.quotesapp.presentation.screens.share_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


@Composable
fun ShareScreen(paddingValues: PaddingValues) {


    Text("Share Screen",modifier= Modifier.padding(paddingValues).background(Color.Black))


}
package com.shalenmathew.quotesapp.presentation.screens.settings_screen.donation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.shalenmathew.quotesapp.presentation.screens.settings_screen.components.CardSection
import com.shalenmathew.quotesapp.presentation.screens.settings_screen.components.donationCardsRow
import com.shalenmathew.quotesapp.presentation.theme.GIFont

@Composable
fun DonationScreen(
    paddingValues: PaddingValues,
    navHost: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Support the Developer",
            fontFamily = GIFont,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            fontSize = 30.sp,
            modifier = Modifier.padding(vertical = 20.dp)
        )

        LazyColumn {
            items(donationCardsRow.size) { index ->
                CardSection(
                    index = index,
                    navHost = navHost,
                    cardsList = donationCardsRow
                )
            }
        }
    }
}

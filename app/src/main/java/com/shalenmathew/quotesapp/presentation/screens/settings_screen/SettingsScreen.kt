package com.shalenmathew.quotesapp.presentation.screens.settings_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shalenmathew.quotesapp.BuildConfig
import com.shalenmathew.quotesapp.presentation.screens.settings_screen.components.CardSection
import com.shalenmathew.quotesapp.presentation.screens.settings_screen.components.cardsRow
import com.shalenmathew.quotesapp.presentation.theme.GIFont
import com.shalenmathew.quotesapp.presentation.theme.Poppins


@Composable
fun SettingsScreen(paddingValues: PaddingValues) {

    Box(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(Color.Black)
    )
    {

        Column(
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .fillMaxWidth()
                .wrapContentHeight()
        )
        {


            Text(
                text = "Settings", fontFamily = GIFont, fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(vertical = 15.dp, horizontal = 15.dp),
                color = Color.White,
                fontSize = 35.sp
            )


            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {

                item {
                    Text(
                        text = "Socials", color = Color.White,
                        modifier = Modifier.padding(start = 12.dp, bottom = 12.dp),
                        fontSize = 20.sp,
                        fontFamily = GIFont, fontWeight = FontWeight.Medium
                    )
                }

                items(cardsRow.size) { cardRowIndex ->
                    CardSection(index = cardRowIndex)
                }

            }

            Spacer(Modifier.weight(1f))

            if (BuildConfig.DEBUG) {

                Box(modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(),
                    contentAlignment = Alignment.Center)
                {
                    Text(
                        text = "made with \uD83E\uDD75 by Shalen Mathew (debug.mode)",
                        fontSize = 12.sp,
                        color = Color.White,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Medium
                    )

                }

            } else {

                Box(modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(),
                    contentAlignment = Alignment.Center)
                {
                    Text(
                        text = "made with \uD83E\uDD75 by Shalen Mathew",
                        fontSize = 12.sp,
                        color = Color.White,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Medium
                    )

                }
            }

        }

    }
    }
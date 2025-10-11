package com.shalenmathew.quotesapp.presentation.screens.about_libraries_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.m3.LibraryDefaults
import com.shalenmathew.quotesapp.presentation.theme.GIFont
import com.shalenmathew.quotesapp.presentation.theme.customGrey2

@Composable
fun AboutLibrariesScreen(
    paddingValues: PaddingValues,
    navHost: NavHostController
) {
    Box(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navHost.navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Text(
                    text = "About Libraries",
                    fontFamily = GIFont,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    fontSize = 35.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            LibrariesContainer(
                modifier = Modifier.fillMaxSize(),
                colors = LibraryDefaults.libraryColors(
                    backgroundColor = Color.Black,
                    contentColor = Color.White,
                    badgeBackgroundColor = customGrey2,
                    badgeContentColor = Color.White
                )
            )
        }
    }
}

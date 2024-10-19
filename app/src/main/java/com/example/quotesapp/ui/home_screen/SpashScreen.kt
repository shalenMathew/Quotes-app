package com.example.quotesapp.ui.home_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.quotesapp.R
import com.example.quotesapp.ui.home_screen.bottom_nav.Screen
import com.example.quotesapp.ui.theme.GIFont
import kotlinx.coroutines.delay



@Composable
fun SplashScreen(navHost: NavHostController) {

    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible=!isVisible
        delay(3000)
        isVisible=!isVisible
        delay(1000)
        navHost.navigate(Screen.Home.route){
            popUpTo(Screen.Splash.route){
                inclusive = true
            }
        }
    }

        AnimatedVisibility(visible = isVisible, enter = fadeIn(), exit = fadeOut()) {

            Box(modifier = Modifier.fillMaxSize()) {

                AsyncImage(
                    model = R.drawable.splash_3,
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop)

                Text(
                    text = "Quotes",
                    color = Color.White,
                    fontSize = 50.sp,
                    fontFamily = GIFont,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.Center)
                )

            }

        }
}
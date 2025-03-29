package com.example.quotesapp.presentation.intro_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.quotesapp.R
import com.example.quotesapp.presentation.home_screen.bottom_nav.Screen
import com.example.quotesapp.presentation.theme.GIFont
import kotlinx.coroutines.delay



@Composable
fun SplashScreen(navHost: NavHostController) {

    var isVisible by remember { mutableStateOf(false) }

        AnimatedVisibility(visible = isVisible, enter = fadeIn(), exit = fadeOut()) {

            Box(modifier = Modifier.fillMaxSize()) {

                Image(
                    painter = painterResource(id = R.drawable.splash_3),
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

    LaunchedEffect(Unit) {
        isVisible=true
        delay(3000)
        isVisible=false
        delay(500)


        // navigate to next screen
        navHost.navigate(Screen.Home.route){
            popUpTo(Screen.Splash.route){
                inclusive = true
            }
        }
    }

}
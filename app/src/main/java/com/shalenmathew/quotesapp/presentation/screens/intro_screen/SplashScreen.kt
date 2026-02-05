package com.shalenmathew.quotesapp.presentation.screens.intro_screen

import androidx.compose.animation.core.EaseOutExpo
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.shalenmathew.quotesapp.R
import com.shalenmathew.quotesapp.presentation.screens.bottom_nav.Screen
import com.shalenmathew.quotesapp.presentation.theme.GIFont
import com.shalenmathew.quotesapp.util.isFirstLaunch
import com.shalenmathew.quotesapp.util.setFirstLaunchDone
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(navHost: NavHostController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // read whether it's first launch
    val isFirstLaunch by context.isFirstLaunch().collectAsState(initial = true)

    var startAnim by remember { mutableStateOf(false) }
    var showTagline by remember { mutableStateOf(false) }

    val fadeAlpha by animateFloatAsState(
        targetValue = if (startAnim) 1f else 0f,
        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing)
    )

    val infiniteGlow = rememberInfiniteTransition(label = "")
    val glowAlpha by infiniteGlow.animateFloat(
        initialValue = 0.8f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    // UI
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.splash_3),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Quotes",
                color = Color.White.copy(alpha = fadeAlpha * glowAlpha),
                fontSize = 48.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = GIFont
            )

            // Only show tagline if it's first launch
            if (showTagline && isFirstLaunch) {
                Spacer(modifier = Modifier.height(20.dp))
                AnimatedTagline(
                    words = listOf("Feel", "the", "words.", "Live", "the", "Meaning"),
                    fontFamily = GIFont
                )
            }
        }
    }

    // Launch animation logic
    LaunchedEffect(isFirstLaunch) {
        startAnim = true
        delay(500)

        if (isFirstLaunch) showTagline = true

        // Adjust delay: longer if tagline visible, shorter otherwise
        delay(if (isFirstLaunch) 3200 else 1000)

        // Navigate to home
        navHost.navigate(Screen.Home.route) {
            popUpTo(Screen.Splash.route) { inclusive = true }
        }

        // After showing once, mark first launch done
        if (isFirstLaunch) {
            scope.launch { context.setFirstLaunchDone() }
        }
    }
}


@Composable
fun AnimatedTagline(words: List<String>, fontFamily: FontFamily) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        words.forEachIndexed { index, word ->
            var showWord by remember { mutableStateOf(false) }

            val alpha by animateFloatAsState(
                targetValue = if (showWord) 1f else 0f,
                animationSpec = tween(600)
            )
            val offsetY by animateDpAsState(
                targetValue = if (showWord) 0.dp else 20.dp,
                animationSpec = tween(600, easing = EaseOutExpo)
            )

            LaunchedEffect(Unit) {
                delay(index * 300L)
                showWord = true
            }

            Text(
                text = "$word ",
                color = Color.White.copy(alpha = alpha),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = fontFamily,
                modifier = Modifier.offset(y = offsetY)
            )
        }
    }
}

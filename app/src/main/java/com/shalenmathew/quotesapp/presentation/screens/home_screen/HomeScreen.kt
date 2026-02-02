package com.shalenmathew.quotesapp.presentation.screens.home_screen

import android.content.Intent
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.gson.Gson
import com.shalenmathew.quotesapp.R
import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.domain.repository.AnimationPreferences
import com.shalenmathew.quotesapp.presentation.screens.bottom_nav.Screen
import com.shalenmathew.quotesapp.presentation.viewmodel.QuoteViewModel
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.delay

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AnimationPreferencesEntryPoint {
    fun animationPreferences(): AnimationPreferences
}

@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    navHost: NavHostController,
    quoteViewModel: QuoteViewModel = hiltViewModel(),
    intent: Intent
) {

    val context = LocalContext.current
    val animationPreferences = remember {
        EntryPointAccessors.fromApplication(
            context,
            AnimationPreferencesEntryPoint::class.java
        ).animationPreferences()
    }

    var isVisible by remember {
        mutableStateOf(animationPreferences.hasRainbowAnimationBeenShown())
    }

    LaunchedEffect(Unit) {
        if (!animationPreferences.hasRainbowAnimationBeenShown()) {
            delay(1000)
            isVisible = true
            animationPreferences.setRainbowAnimationShown()
        }
    }

    LaunchedEffect(Unit) {
        // NAVIGATING TO SHARE SCREEN FROM NOTIFICATION
        if (intent.getStringExtra("shortcut_nav") == "share") {
            navHost.currentBackStackEntry?.savedStateHandle?.set(
                "quote",
                Gson().fromJson(intent.getStringExtra("quote"), Quote::class.java)
            )
            intent.removeExtra("shortcut_nav")
            intent.removeExtra("quote")
            navHost.navigate(Screen.Share.route)
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black))
    {

        val painter = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            rememberAsyncImagePainter(R.drawable.bg)
        } else {
            painterResource(R.drawable.bg)
        }

        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(animationSpec = tween(durationMillis = 3000)),
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.TopEnd),
        ) {

            Image(
                painter = painter,
                contentDescription = null
            )
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .padding(paddingValues)
        ) {

            QuoteOfTheDaySection(quoteViewModel)
            QuoteItemListSection(quoteViewModel, navHost)
        }
    }

}

package com.shalenmathew.quotesapp.presentation.screens.home_screen

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
import com.shalenmathew.quotesapp.R
import com.shalenmathew.quotesapp.domain.repository.AnimationPreferences
import com.shalenmathew.quotesapp.presentation.viewmodel.QuoteViewModel
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.delay

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AnimationPreferencesEntryPoint {
    fun animationPreferences(): AnimationPreferences
}

@Composable
fun HomeScreen(paddingValues: PaddingValues,
               navHost: NavHostController,
               quoteViewModel: QuoteViewModel= hiltViewModel()
){

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

    Box(modifier = Modifier.fillMaxSize().background(Color.Black))
    {

        val painter = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            rememberAsyncImagePainter(R.drawable.bg)
        } else {
            painterResource(R.drawable.bg)
        }

        AnimatedVisibility(visible = isVisible,
            enter = fadeIn(animationSpec = tween(durationMillis = 3000)),
            modifier = Modifier.size(200.dp).align(Alignment.TopEnd),
        ) {

            Image(painter = painter,
                contentDescription = null
                )
        }


        Column(modifier = Modifier.fillMaxSize()
            .background(Color.Transparent)
            .padding(paddingValues)) {

            QuoteOfTheDaySection(quoteViewModel)
            QuoteItemListSection(quoteViewModel,navHost)
        }
    }

}

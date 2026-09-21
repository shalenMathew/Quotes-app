package com.shalenmathew.quotesapp.presentation.screens.home_screen

import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
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
import com.shalenmathew.quotesapp.presentation.screens.library.AddToCollectionBottomSheet
import com.shalenmathew.quotesapp.presentation.viewmodel.QuoteViewModel
import com.shalenmathew.quotesapp.util.hasShownCollectionTip
import com.shalenmathew.quotesapp.util.setCollectionTipShown
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

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
    val haptic = LocalHapticFeedback.current
    val animationPreferences = remember {
        EntryPointAccessors.fromApplication(
            context,
            AnimationPreferencesEntryPoint::class.java
        ).animationPreferences()
    }

    val state = quoteViewModel.quoteState.value
    val scope = rememberCoroutineScope()
    var selectedQuoteForCollection by remember { mutableStateOf<Quote?>(null) }

    var isVisible by remember {
        mutableStateOf(animationPreferences.hasRainbowAnimationBeenShown())
    }

    // Trigger rainbow animation logic
    LaunchedEffect(state.isLoading, state.error) {
        if (state.isLoading) {
            // Only fade out if it's currently visible AND it's not the initial startup load
            if (isVisible && animationPreferences.hasRainbowAnimationBeenShown()) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                isVisible = false
            }
        } else {
            // Data has arrived OR error occurred
            // Show (fade in) if it's currently hidden
            if (!isVisible && (state.dataList.isNotEmpty() || state.error.isNotEmpty())) {
                delay(500)
                isVisible = true
                // Mark as shown for the current session if data was fetched
                if (state.dataList.isNotEmpty() && !animationPreferences.hasRainbowAnimationBeenShown()) {
                    animationPreferences.setRainbowAnimationShown()
                }
            }
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    )
    {

        val painter = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            rememberAsyncImagePainter(R.drawable.rainbow_2)
        } else {
            painterResource(R.drawable.rainbow_2)
        }

        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(animationSpec = tween(durationMillis = 2000)),
            exit = fadeOut(animationSpec = tween(durationMillis = 1500)),
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.TopEnd),
        ) {

            Image(
                painter = painter,
                contentDescription = null
            )
        }


        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .padding(paddingValues)
        ) {
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(durationMillis = 2000)),
                exit = fadeOut(animationSpec = tween(durationMillis = 1500)),
                modifier = Modifier.fillMaxSize()
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    QuoteOfTheDaySection(quoteViewModel)
                    QuoteItemListSection(
                        quoteViewModel = quoteViewModel,
                        navHost = navHost,
                        onLikeClick = {
                            scope.launch {
                                if (!context.hasShownCollectionTip().first()) {
                                    Toast.makeText(
                                        context,
                                        "Hold heart to add in collection",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    context.setCollectionTipShown()
                                }
                            }
                        },
                        onLongClickHeart = { quote ->
                            selectedQuoteForCollection = quote
                        }
                    )
                }
            }

            if (state.isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        selectedQuoteForCollection?.let { quote ->
            AddToCollectionBottomSheet(
                quote = quote,
                onDismiss = { selectedQuoteForCollection = null }
            )
        }
    }

}

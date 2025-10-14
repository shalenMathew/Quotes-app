package com.shalenmathew.quotesapp.presentation

import androidx.lifecycle.lifecycleScope
import com.shalenmathew.quotesapp.util.getSavedWidgetQuoteObject
import com.shalenmathew.quotesapp.util.saveWidgetQuoteObject
import kotlinx.coroutines.launch
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.shalenmathew.quotesapp.presentation.navigation.AppNavigation
import com.shalenmathew.quotesapp.presentation.screens.bottom_nav.BottomNavAnimation
import com.shalenmathew.quotesapp.presentation.screens.bottom_nav.Screen
import com.shalenmathew.quotesapp.presentation.theme.QuotesAppTheme
import com.shalenmathew.quotesapp.presentation.workmanager.notification.ScheduleNotification
import com.shalenmathew.quotesapp.presentation.workmanager.widget.ScheduleWidgetRefresh
import com.shalenmathew.quotesapp.util.Constants
import com.shalenmathew.quotesapp.util.checkWorkManagerStatus
import com.shalenmathew.quotesapp.domain.usecases.fav_screen_usecases.FavQuoteUseCase
import com.shalenmathew.quotesapp.presentation.widget.WidgetActionReceiver
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
// used android entry here as we are injecting the viewmodel using hilt
// even when not explicitly mention @inject the viewmodel creation and its factory creation is taken care by hilt behind the scenes
// so the activities or fragments which needs to be injected by hilt should be annotated using this annotation
class MainActivity : ComponentActivity() {

    companion object {
        const val ACTION_LIKE_QUOTE = "com.shalenmathew.quotesapp.action.LIKE_QUOTE"
    }

    @Inject lateinit var scheduleNotification:ScheduleNotification
    @Inject lateinit var scheduleWidget: ScheduleWidgetRefresh
    @Inject lateinit var favQuoteUseCase: FavQuoteUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Handle widget save to favorites action
        intent?.let { handleWidgetSaveToFavoritesIntent(it) }

        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.auto(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT)
        )
        setContent {
            QuotesAppTheme {

                scheduleNotification.scheduleNotification()


                Handler(Looper.getMainLooper()).postDelayed({
                    scheduleWidget.scheduleWidgetRefresh()
                }, 5000)


                /* REQUESTING NECESSARY PERMISSIONS  */
                requestNecessaryPermissions()
                checkWorkManagerStatus(this,this)

                val navHost = rememberNavController()

                Scaffold(
                    containerColor = androidx.compose.ui.graphics.Color.Black, bottomBar = {

                    val currentBackStackEntry by navHost.currentBackStackEntryAsState()
                    val currentDestination = currentBackStackEntry?.destination?.route

                 val currentScreen = Screen.values.firstOrNull{ it->
                        it.route==currentDestination
                    }

                    currentScreen?.let {
                        if (it.needBottomNav){
                            BottomNavAnimation(navHost)
                        }
                    }?:run {
                        Log.d("TAG","currentScreen = $currentScreen")
                    }

                })
                { paddingValues ->
                    // CHANGE APP NAVIGATION
                    AppNavigation(navHost = navHost, paddingValues = paddingValues)
                }

            }
        }
    }

    fun requestNecessaryPermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission()
        }


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            requestWriteExternalStoragePermission()
        }

    }

    private fun requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), Constants.REQUEST_CODE_NOTIFICATION)
        }
    }

    private fun requestWriteExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), Constants.REQUEST_CODE_WRITE_STORAGE)
        }
    }

    override fun onNewIntent(intent: android.content.Intent?) {
        super.onNewIntent(intent)
        intent?.let { handleWidgetSaveToFavoritesIntent(it) }
    }

    private fun handleWidgetSaveToFavoritesIntent(intent: android.content.Intent) {
        when (intent?.action) {
            WidgetActionReceiver.ACTION_SAVE_TO_FAVORITES -> {
                Log.d("MainActivity", "Handling save to favorites from widget")
                lifecycleScope.launch {
                    try {
                        val widgetQuote = getSavedWidgetQuoteObject().first()
                        if (widgetQuote != null && widgetQuote.liked) {
                            // Save to favorites using the repository
                            // For now, we'll just log it since we don't have direct access to the repository here
                            Log.d("SaveToFavorites", "Would save to favorites: ${widgetQuote.quote}")
                            // TODO: Implement proper favorites saving using repository
                        }
                    } catch (e: Exception) {
                        Log.e("MainActivity", "Error saving to favorites", e)
                    }
                }
            }
            ACTION_LIKE_QUOTE -> {
                Log.d("MainActivity", "Handling like quote from widget")
                lifecycleScope.launch {
                    try {
                        // Get the current widget quote
                        val widgetQuote = getSavedWidgetQuoteObject().first()
                        if (widgetQuote != null) {
                            // Toggle the like state in the data store (without saving to favorites)
                            val updatedQuote = widgetQuote.copy(liked = !widgetQuote.liked)
                            saveWidgetQuoteObject(updatedQuote)
                            
                            // Use the injected use case to save to favorites if now liked
                            if (updatedQuote.liked) {
                                favQuoteUseCase.favLikedQuote(updatedQuote)
                                Log.d("MainActivity", "Quote saved to favorites: ${widgetQuote.quote}")
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("MainActivity", "Error liking quote from widget", e)
                    }
                }
            }
        }
    }



}

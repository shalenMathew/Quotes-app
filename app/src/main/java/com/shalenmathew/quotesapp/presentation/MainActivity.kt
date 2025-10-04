package com.shalenmathew.quotesapp.presentation

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
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
// used android entry here as we are injecting the viewmodel using hilt
// even when not explicitly mention @inject the viewmodel creation and its factory creation is taken care by hilt behind the scenes
// so the activities or fragments which needs to be injected by hilt should be annotated using this annotation
class MainActivity : ComponentActivity() {

    @Inject lateinit var scheduleNotification:ScheduleNotification
    @Inject lateinit var scheduleWidget: ScheduleWidgetRefresh

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

                    // Find the current screen, with fallback to Home if not found
                    val currentScreen = Screen.values.firstOrNull { it.route == currentDestination } 
                        ?: Screen.Home // Fallback to Home screen to prevent bottom bar from disappearing

                    // Only show bottom navigation for screens that need it
                    if (currentScreen.needBottomNav) {
                        BottomNavAnimation(navHost)
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

    private fun requestWriteExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), Constants.REQUEST_CODE_WRITE_STORAGE)
        }
    }

    private fun requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), Constants.REQUEST_CODE_NOTIFICATION)
        }
    }

}




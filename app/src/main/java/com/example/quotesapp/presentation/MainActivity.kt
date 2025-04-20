package com.example.quotesapp.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.quotesapp.BuildConfig
import com.example.quotesapp.presentation.navigation.AppNavigation
import com.example.quotesapp.presentation.screens.fav_screen.FavScreen
import com.example.quotesapp.presentation.screens.home_screen.HomeScreen
import com.example.quotesapp.presentation.screens.home_screen.bottom_nav.BottomNavAnimation
import com.example.quotesapp.presentation.screens.home_screen.bottom_nav.Screen
import com.example.quotesapp.presentation.screens.intro_screen.SplashScreen
import com.example.quotesapp.presentation.screens.share_screen.ShareScreen
import com.example.quotesapp.presentation.theme.QuotesAppTheme
import com.example.quotesapp.presentation.workmanager.notification.ScheduleNotification
import com.example.quotesapp.presentation.workmanager.widget.ScheduleWidgetRefresh
import com.example.quotesapp.util.Constants
import com.example.quotesapp.util.checkWorkManagerStatus
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
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
            statusBarStyle = SystemBarStyle.light(Color.BLACK,Color.BLACK),
            navigationBarStyle = SystemBarStyle.light(Color.BLACK,Color.BLACK)
        )
        setContent {
            QuotesAppTheme {

                var firebaseAnalytics:FirebaseAnalytics = Firebase.analytics

                if (BuildConfig.DEBUG) {
                    Log.d("TAG","DEBUGGING MODE")
                    firebaseAnalytics.setAnalyticsCollectionEnabled(false)
                } else {
                    Log.d("TAG","RELEASE MODE")
                    firebaseAnalytics.setAnalyticsCollectionEnabled(true)
                }

                scheduleNotification.scheduleNotification()


                Handler(Looper.getMainLooper()).postDelayed({
                    scheduleWidget.scheduleWidgetRefresh()
                }, 5000)


                /* REQUESTING NECESSARY PERMISSIONS  */
                requestNecessaryPermissions()
                checkWorkManagerStatus(this,this)

                val navHost = rememberNavController()

                Scaffold(bottomBar = {

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




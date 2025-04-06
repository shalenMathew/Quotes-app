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
import androidx.activity.viewModels
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.work.WorkManager
import com.example.quotesapp.QuoteApplication
import com.example.quotesapp.R
import com.example.quotesapp.presentation.fav_screen.FavScreen
import com.example.quotesapp.presentation.home_screen.HomeScreen
import com.example.quotesapp.presentation.home_screen.bottom_nav.BottomNavAnimation
import com.example.quotesapp.presentation.home_screen.bottom_nav.Screen
import com.example.quotesapp.presentation.intro_screen.SplashScreen
import com.example.quotesapp.presentation.theme.QuotesAppTheme
import com.example.quotesapp.presentation.viewmodel.QuoteViewModel
import com.example.quotesapp.presentation.workmanager.notification.ScheduleNotification
import com.example.quotesapp.presentation.workmanager.widget.ScheduleWidgetRefresh
import com.example.quotesapp.util.Constants
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
// used android entry here as we are injecting the viewmodel using hilt
// even when not explicitly mention @inject the viewmodel creation and its factory creation is taken care by hilt behind the scenes
// so the activities or fragments which needs to be injected by hilt should be annotated using this annotation
class MainActivity : ComponentActivity() {

    private lateinit var firebaseAnalytics:FirebaseAnalytics

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

                FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.d("TAG", "Fetching FCM registration token failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new FCM registration token
                    val token = task.result


                    Log.d("TAG", "Token - $token")

                })


                firebaseAnalytics = (application as QuoteApplication).firebaseAnalytics
                scheduleNotification.scheduleNotification()
//                scheduleWidget.scheduleWidgetRefresh()

                Handler(Looper.getMainLooper()).postDelayed({
                    scheduleWidget.scheduleWidgetRefresh()
                }, 5000)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestNotificationPermission()
                }

                val navHost = rememberNavController()
                checkWorkManagerStatus()

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

                 val quoteViewModel:QuoteViewModel by viewModels()

                    NavHost(navController = navHost, startDestination = Screen.Splash.route){
                        composable(Screen.Splash.route){ SplashScreen(navHost) }
                        composable(Screen.Home.route){  HomeScreen(paddingValues,quoteViewModel) }
                        composable(Screen.Fav.route){ FavScreen(paddingValues) }

                    }

                }


            }
        }
    }

    private fun checkWorkManagerStatus() {

        val workManager = WorkManager.getInstance(this)

        workManager.getWorkInfosForUniqueWorkLiveData("quotes_widget_update").observe(this) { workInfoList ->
            if (workInfoList.isNotEmpty()) {
                for (workInfo in workInfoList) {
                    Log.d("WorkManagerStatus", "Work State: ${workInfo.state}")
                }
            } else {
                Log.d("WorkManagerStatus", "No Work Found")
            }
        }


        workManager.getWorkInfosForUniqueWorkLiveData("quotes_notification").observe(this) { workInfoList ->
            if (workInfoList.isNotEmpty()) {
                for (workInfo in workInfoList) {
                    Log.d(Constants.WORK_MANAGER_STATUS_NOTIFY, "Work State: ${workInfo.state}")
                }
            } else {
                Log.d(Constants.WORK_MANAGER_STATUS_NOTIFY, "No Work Found")
            }
        }

    }

    private fun requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), Constants.REQUEST_CODE)
        }
    }

}




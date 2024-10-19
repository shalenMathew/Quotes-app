package com.example.quotesapp.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.quotesapp.ui.fav_screen.FavScreen
import com.example.quotesapp.ui.home_screen.HomeScreen
import com.example.quotesapp.ui.home_screen.SplashScreen
import com.example.quotesapp.ui.home_screen.bottom_nav.BottomNavAnimation
import com.example.quotesapp.ui.home_screen.bottom_nav.Screen
import com.example.quotesapp.ui.theme.QuotesAppTheme
import com.example.quotesapp.ui.viewmodel.QuoteViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
// used android entry here as we are injecting the viewmodel using hilt
// even when not explicitly mention @inject the viewmodel creation and its factory creation is taken care by hilt behind the scenes
// so the activities or fragments which needs to be injected by hilt should be annotated using this annotation
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.BLACK,Color.BLACK),
            navigationBarStyle = SystemBarStyle.light(Color.BLACK,Color.BLACK)
        )
        setContent {
            QuotesAppTheme {

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

                }) { paddingValues ->

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
}


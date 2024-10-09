package com.example.quotesapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Scaffold
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quotesapp.ui.fav_screen.FavScreen
import com.example.quotesapp.ui.home_screen.HomeScreen
import com.example.quotesapp.ui.home_screen.bottom_nav.BottomNavNoAnimation
import com.example.quotesapp.ui.home_screen.bottom_nav.Screen
import com.example.quotesapp.ui.theme.QuotesAppTheme
import com.example.quotesapp.ui.viewmodel.QuoteViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuotesAppTheme {

                val navHost = rememberNavController()

                Scaffold(bottomBar = {BottomNavNoAnimation(navHost)}) { paddingValues ->

                 val quoteViewModel:QuoteViewModel by viewModels()

                    NavHost(navController = navHost, startDestination = Screen.Home.route){

                        composable(Screen.Home.route){  HomeScreen(paddingValues,quoteViewModel) }
                        composable(Screen.Fav.route){ FavScreen(paddingValues) }

                    }

                }
            }
        }
    }
}


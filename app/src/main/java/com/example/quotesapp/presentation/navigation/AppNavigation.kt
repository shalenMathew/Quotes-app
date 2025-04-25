package com.example.quotesapp.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.quotesapp.presentation.screens.fav_screen.FavScreen
import com.example.quotesapp.presentation.screens.home_screen.HomeScreen
import com.example.quotesapp.presentation.screens.home_screen.bottom_nav.Screen
import com.example.quotesapp.presentation.screens.intro_screen.SplashScreen
import com.example.quotesapp.presentation.screens.settings_screen.SettingsScreen
import com.example.quotesapp.presentation.screens.share_screen.ShareScreen

@Composable
fun AppNavigation(navHost: NavHostController,paddingValues: PaddingValues){

    NavHost(navController = navHost, startDestination = Screen.Splash.route){
        composable(Screen.Splash.route){ SplashScreen(navHost) }
        composable(Screen.Home.route){  HomeScreen(paddingValues = paddingValues, navHost = navHost) }
        composable(Screen.Fav.route){ FavScreen(paddingValues = paddingValues, navHost = navHost) }
        composable(Screen.Share.route) { ShareScreen(paddingValues, navHost) }
        composable(Screen.Settings.route) { SettingsScreen(paddingValues = paddingValues) }
    }

}
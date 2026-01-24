package com.shalenmathew.quotesapp.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.shalenmathew.quotesapp.presentation.screens.about_libraries_screen.AboutLibrariesScreen
import com.shalenmathew.quotesapp.presentation.screens.fav_screen.FavScreen
import com.shalenmathew.quotesapp.presentation.screens.home_screen.HomeScreen
import com.shalenmathew.quotesapp.presentation.screens.bottom_nav.Screen
import com.shalenmathew.quotesapp.presentation.screens.custom_quote.AddCustomQuoteScreen
import com.shalenmathew.quotesapp.presentation.screens.intro_screen.SplashScreen
import com.shalenmathew.quotesapp.presentation.screens.more_apps.MoreApps
import com.shalenmathew.quotesapp.presentation.screens.settings_screen.SettingsScreen
import com.shalenmathew.quotesapp.presentation.screens.share_screen.ShareScreen

@Composable
fun AppNavigation(navHost: NavHostController,paddingValues: PaddingValues,startDestination: String = Screen.Splash.route){

    NavHost(navController = navHost, startDestination = startDestination){
        composable(Screen.Splash.route){ SplashScreen(navHost) }
        composable(Screen.Home.route){  HomeScreen(paddingValues = paddingValues, navHost = navHost) }
        composable(Screen.Fav.route){ FavScreen(paddingValues = paddingValues, navHost = navHost) }
        composable(Screen.Share.route) { ShareScreen(paddingValues, navHost) }
        composable(Screen.Settings.route) { SettingsScreen(paddingValues = paddingValues, navHost = navHost) }
        composable(Screen.AboutLibraries.route) { AboutLibrariesScreen(paddingValues = paddingValues, navHost = navHost) }
        composable(Screen.MoreApps.route) { MoreApps(paddingValues = paddingValues, navHost = navHost)}
        composable(Screen.AddCustomQuote.route) { AddCustomQuoteScreen(paddingValues = paddingValues, navHost = navHost) }
    }

}
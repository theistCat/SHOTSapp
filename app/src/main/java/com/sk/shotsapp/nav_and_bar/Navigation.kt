package com.sk.shotsapp.nav_and_bar

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sk.shotsapp.screens.*

@Composable
fun Navigation() {
    val darkTheme = isSystemInDarkTheme()
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) },
        topBar = { TopBar(navController = navController) },
        backgroundColor = colors.primary,
    ) {

        NavHost(navController = navController, startDestination = "splash") {
            composable("splash") { SplashScreen(navController = navController) }
            composable(NavigationItem.Home.route) { HomeScreen(darkTheme = darkTheme) }
            composable(NavigationItem.Map.route) { MapScreen(darkTheme = darkTheme) }
            composable(NavigationItem.Event.route) { EventScreen(darkTheme = darkTheme) }
            composable("profile") { ProfileScreen(darkTheme = darkTheme) }
            composable("settings") { SettingScreen(darkTheme = darkTheme) }
        }
    }
}
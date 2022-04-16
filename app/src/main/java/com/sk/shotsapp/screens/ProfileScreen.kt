package com.sk.shotsapp.screens

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sk.shotsapp.AppViewModel
import com.sk.shotsapp.NavigateBetweenScreen
import com.sk.shotsapp.nav_and_bar.NavigationEnum
import com.sk.shotsapp.nav_and_bar.TopBar


@Composable
fun ProfileScreen(loginViewModel: AppViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val backstackEntry = navController.currentBackStackEntryAsState()

    val currentScreen = NavigationEnum.fromRoute(
        backstackEntry.value?.destination?.route,
        loginViewModel.isLoggedIn
    )
    Scaffold(
        topBar = { TopBar(navController, currentScreen) },
    ) {
        NavigateBetweenScreen(navController)
    }
}

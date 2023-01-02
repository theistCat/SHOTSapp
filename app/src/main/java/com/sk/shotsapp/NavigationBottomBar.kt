package com.sk.shotsapp

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.sk.shotsapp.screens.*

@Composable
fun NaviG(viewModel: AppViewModel = hiltViewModel(), fusedLocationProviderClient: FusedLocationProviderClient) {
    val navControllerMain = rememberNavController()
    val items = listOf(
        Screen.Chat, Screen.Home, Screen.Profile
    )
    Scaffold(bottomBar = {
        if (viewModel.isBottomBarEnabled.value) {
            BottomNavigation(
                backgroundColor = Color.White
            ) {
                val navBackStackEntry by navControllerMain.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    BottomNavigationItem(icon = {
                        Icon(
                            painter = painterResource(id = screen.resourceId),
                            contentDescription = screen.route,
                            modifier = Modifier.size(if (screen.route == "home") 45.dp else 30.dp)
                        )
                    },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navControllerMain.navigate(screen.route) {
                                popUpTo(navControllerMain.graph.findStartDestination().id) {
                                    saveState = false
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        })
                }

            }
        }
    }) { innerPadding ->
        NavHost(
            navController = navControllerMain,
            startDestination = if (viewModel.isLoggedIn.value) "home" else "Login",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("profile") { Profile(viewModel, navControllerMain) }
            composable("home") {
                HomeScreen(navControllerMain, viewModel, fusedLocationProviderClient)
            }
            composable("chat") { ChatScreen() }
            composable("create") { CreateNew(viewModel, navControllerMain, fusedLocationProviderClient) }
            composable("settings") { SettingScreen(viewModel, navControllerMain) }
            composable("changeAccount") { ChangeAccountInfo(viewModel, navControllerMain) }
            composable("Login") {
                viewModel.setError("")
                LoginScreen(
                    viewModel = viewModel, navControllerMain
                )
            }
            composable(route = "Sign in with Google") {
                viewModel.setError("")
                EmailLoginScreen(viewModel)
            }

            /*...*/
        }
    }
}

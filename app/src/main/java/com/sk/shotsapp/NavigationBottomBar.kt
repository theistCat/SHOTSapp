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
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sk.shotsapp.screens.*

@Composable
fun NaviG(viewModel: AppViewModel) {
    val navControllerMain = rememberNavController()
    val items = listOf(
//        Screen.Events,
        Screen.Chat,
        Screen.Home,
        Screen.Profile
    )
    Scaffold(
        bottomBar = {
            BottomNavigation(
//                backgroundColor = ifDarkTheme(status = true)
                backgroundColor = Color.White
            ) {
                val navBackStackEntry by navControllerMain.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = screen.resourceId),
//                                tint = BarColor,
                                contentDescription = screen.route,
                                modifier = Modifier.size(if (screen.route == "home") 45.dp else 30.dp)
                            )
                        },
//                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navControllerMain.navigate(screen.route) {
                                popUpTo(navControllerMain.graph.findStartDestination().id) {
                                    saveState = false
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navControllerMain,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("profile") { Profile(viewModel, navControllerMain) }
            composable("home") { HomeScreen(navControllerMain, viewModel) }
//            composable("events") { EventScreen(viewModel) }
            composable("chat") { ChatScreen() }
            composable("create") {
                if (viewModel.isLoggedIn.value) CreateNew(viewModel, navControllerMain)
                else Profile(viewModel, navControllerMain)
            }
            composable("settings") { SettingScreen(viewModel, navControllerMain) }
            /*...*/
        }
    }
}

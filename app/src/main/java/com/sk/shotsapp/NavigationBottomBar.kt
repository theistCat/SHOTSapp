package com.sk.shotsapp

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
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
import com.sk.shotsapp.ui.theme.Shapes

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
            if (viewModel.isBottomBarEnabled.value) {
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
        }
    ) { innerPadding ->
        NavHost(
            navController = navControllerMain,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
//            val startDestination = if (viewModel.isLoggedIn.value) "Welcome" else "Login"

            composable("profile") {
                if (viewModel.isLoggedIn.value) Profile(
                    viewModel,
                    navControllerMain
                )
                else LoginScreen(viewModel = viewModel, navController = navControllerMain)
            }
            composable("home") { HomeScreen(navControllerMain, viewModel) }
//            composable("events") { EventScreen(viewModel) }
            composable("chat") { ChatScreen() }
            composable("create") {
                if (viewModel.isLoggedIn.value) CreateNew(viewModel, navControllerMain)
                else Profile(viewModel, navControllerMain)
            }
            composable("settings") { SettingScreen(viewModel, navControllerMain) }
            composable("changeAccount") { ChangeAccountInfo(viewModel, navControllerMain) }
            composable("createAccount") { CreateAccount(viewModel, navControllerMain) }
            composable(route = "Login") {
                viewModel.setError("")
                LoginScreen(
                    viewModel = viewModel, navControllerMain
                )
            }
            composable(route = "Sign in with Google") {
                viewModel.setError("")
                EmailLoginScreen(viewModel, navControllerMain)
            }
//            composable(route = "Welcome") { WelcomeScreen(viewModel, navControllerMain) }

            /*...*/
        }
    }
}

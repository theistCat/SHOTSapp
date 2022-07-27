package com.sk.shotsapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sk.shotsapp.screens.*
import com.sk.shotsapp.ui.theme.ifDarkTheme

@Composable
fun NaviG(viewModel: AppViewModel) {
    val navControllerMain = rememberNavController()
    val items = listOf(
        Screen.Home,
//        Screen.Events,
        Screen.Chat,
        Screen.Profile
    )
    Scaffold(
        bottomBar = {
            BottomNavigation(
                backgroundColor = ifDarkTheme(status = true)
            ) {
                val navBackStackEntry by navControllerMain.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = screen.resourceId),
//                                painter = if (viewModel.isLoggedIn.value && Firebase.auth.currentUser?.photoUrl != null && screen.route == "profile") rememberAsyncImagePainter(
//                                    Firebase.auth.currentUser?.photoUrl
//                                )
//                                else painterResource(id = screen.resourceId),
                                contentDescription = screen.route
                            )
                        },
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navControllerMain.navigate(screen.route) {
//                                // Pop up to the start destination of the graph to
//                                // avoid building up a large stack of destinations
//                                // on the back stack as users select items
                                popUpTo(navControllerMain.graph.findStartDestination().id) {
                                    saveState = false
                                }
//                                // Avoid multiple copies of the same destination when
//                                // re-selecting the same item
                                launchSingleTop = true
//                                // Restore state when re-selecting a previously selected item
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

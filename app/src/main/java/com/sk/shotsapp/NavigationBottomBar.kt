package com.sk.shotsapp

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sk.shotsapp.screens.*
import com.sk.shotsapp.ui.theme.ifDarkTheme

@Composable
fun NaviG(viewModel: AppViewModel) {
    val navControllerMain = rememberNavController()
    val items = listOf(
        Screen.Home,
        Screen.Events,
        Screen.Chat,
        Screen.Profile
    )
    Scaffold(
//        topBar = { RoundedProfileIcon(navControllerMain = navControllerMain) },
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
            composable("home") { HomeScreen(navControllerMain) }
            composable("events") { EventScreen() }
            composable("chat") { ChatScreen() }
            composable("create") { CreateNew() }
            composable("settings") { SettingScreen(viewModel, navControllerMain) }
            /*...*/
        }
    }
}

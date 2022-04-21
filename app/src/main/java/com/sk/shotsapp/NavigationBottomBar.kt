package com.sk.shotsapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.sk.shotsapp.screens.EventScreen
import com.sk.shotsapp.screens.HomeScreen

@Composable
fun NaviG(viewModel: AppViewModel) {
    val navControllerMain = rememberNavController()
    val items = listOf(
        Screen.Home,
        Screen.Events,
    )
    Scaffold(
        topBar = { RoundedProfileIcon(navControllerMain = navControllerMain) },
        bottomBar = {
            BottomNavigation {
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
            composable("profile") { Profile(viewModel) }
            composable("home") { HomeScreen() }
            composable("events") { EventScreen() }
            /*...*/
        }
    }
}

@Composable
fun RoundedProfileIcon(navControllerMain: NavController) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Profile Icon",
            modifier = Modifier
                .padding(top = 16.dp, end = 16.dp)
                .size(40.dp)
                .clip(CircleShape)
                .border(BorderStroke(2.dp, Color.Magenta), shape = CircleShape)
                .align(Alignment.CenterEnd)
                .clickable { navControllerMain.navigate("profile") }
        )
    }
}
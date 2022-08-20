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
import com.google.android.gms.location.FusedLocationProviderClient
import com.sk.shotsapp.screens.*

@Composable
fun NaviG(viewModel: AppViewModel, fusedLocationProviderClient: FusedLocationProviderClient) {
    val navControllerMain = rememberNavController()
    val items = listOf(
//        Screen.Events,
        Screen.Chat, Screen.Home, Screen.Profile
    )
    Scaffold(bottomBar = {
        if (viewModel.isBottomBarEnabled.value) {
            BottomNavigation(
//                backgroundColor = ifDarkTheme(status = true)
                backgroundColor = Color.White
            ) {
                val navBackStackEntry by navControllerMain.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    BottomNavigationItem(icon = {
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
                        })
                }

            }
        }
    }) { innerPadding ->
//        viewModel.db.collection("users").get().addOnSuccessListener { result ->
//            viewModel.email.clear()
//            viewModel.name.clear()
//            viewModel.age.clear()
//            viewModel.sex.clear()
//            for (document in result) {
//                viewModel.sex.add(document["sex"].toString())
//                viewModel.age.add(document["age"].toString())
//                viewModel.email.add(document["email"].toString())
//                viewModel.name.add(document["name"].toString())
//            }
//        }
        NavHost(
            navController = navControllerMain,
            startDestination = if (viewModel.isLoggedIn.value) "profile" else "Login",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("profile") { Profile(viewModel, navControllerMain) }
            composable("home") {
                if (viewModel.isNewUser.value) ChangeAccountInfo(
                    viewModel = viewModel, navController = navControllerMain
                ) else HomeScreen(navControllerMain, viewModel, fusedLocationProviderClient)
            }
//            composable("events") { EventScreen(viewModel) }
            composable("chat") { ChatScreen() }
            composable("create") {
//                if (viewModel.isLoggedIn.value)
                CreateNew(viewModel, navControllerMain)
//                else
//                    Profile(viewModel, navControllerMain)
            }
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
                EmailLoginScreen(viewModel, navControllerMain)
            }
//            composable(route = "Welcome") { WelcomeScreen(viewModel, navControllerMain) }

            /*...*/
        }
    }
}

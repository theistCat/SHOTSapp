package com.sk.shotsapp.nav_and_bar

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.sk.shotsapp.NavigateBackButton

@Composable
fun TopBar(navController: NavHostController, currentScreen: NavigationEnum) {
    TopAppBar(
        title = { Text(text = stringResource(currentScreen.title)) },
        // To avoid going back to previous screen after login/logout click
        navigationIcon = {
            if (currentScreen != NavigationEnum.Welcome
                && currentScreen != NavigationEnum.Login
            ) {
                NavigateBackButton(navController)
            }
        }
    )
}



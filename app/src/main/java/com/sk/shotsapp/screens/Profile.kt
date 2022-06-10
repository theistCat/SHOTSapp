package com.sk.shotsapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.sk.shotsapp.AppViewModel
import com.sk.shotsapp.NavigateBetweenScreen
import com.sk.shotsapp.R

@Composable
fun Profile(loginViewModel: AppViewModel, navControllerMain: NavController) {
    Scaffold(
        topBar = { SettingsIcon(navControllerMain = navControllerMain) }
    ) {
        val navController = rememberNavController()
        NavigateBetweenScreen(navController = navController, loginViewModel = loginViewModel)
    }
}


@Composable
fun SettingsIcon(navControllerMain: NavController) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(id = R.drawable.ic_settings),
            contentDescription = "Profile Icon",
            modifier = Modifier
                .padding(top = 16.dp, end = 16.dp)
                .size(30.dp)
                .clip(CircleShape)
//                .border(BorderStroke(2.dp, Color.Magenta), shape = CircleShape)
                .align(Alignment.CenterEnd)
                .clickable { navControllerMain.navigate("settings") }
        )
    }
}
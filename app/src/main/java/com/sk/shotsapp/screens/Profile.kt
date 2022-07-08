package com.sk.shotsapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sk.shotsapp.AppViewModel
import com.sk.shotsapp.NavigateBetweenScreen
import com.sk.shotsapp.R
import com.sk.shotsapp.Screen

@Composable
fun Profile(loginViewModel: AppViewModel, navControllerMain: NavController) {
    Scaffold(
        topBar = {
            ProfileTopBar(
                navControllerMain = navControllerMain,
                loginViewModel = loginViewModel
            )
        }
    ) {
        val navController = rememberNavController()
        Column(Modifier.fillMaxSize()) {
            if (loginViewModel.isLoggedIn.value) Avatar()
            NavigateBetweenScreen(
                navController = navController,
                loginViewModel = loginViewModel
            )
        }
        print(it)
    }
}

@Composable
fun ProfileTopBar(navControllerMain: NavController, loginViewModel: AppViewModel) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Title(
            whichScreen = if (loginViewModel.isLoggedIn.value) "${
                if (FirebaseAuth.getInstance().currentUser?.displayName != null)
                    Firebase.auth.currentUser?.displayName
                else
                    Firebase.auth.currentUser?.email?.dropLast(10)
            }" else Screen.Profile.label
        )
        SettingsIcon(navControllerMain = navControllerMain)
    }
}

@Composable
fun SettingsIcon(navControllerMain: NavController) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Icon(
            painter = painterResource(id = R.drawable.ic_settings),
            contentDescription = "Profile Icon",
            modifier = Modifier
                .padding(top = 8.dp, end = 8.dp)
                .size(30.dp)
                .clip(CircleShape)
//                .border(BorderStroke(2.dp, Color.Magenta), shape = CircleShape)
                .align(Alignment.CenterEnd)
                .clickable { navControllerMain.navigate("settings") },
            tint = if (isSystemInDarkTheme()) Color.White else Color.Black
        )
    }
}

@Composable
fun Avatar() {

    Image(
        painterResource(id = R.drawable.selyn_cat),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentDescription = "",
        alignment = Alignment.Center,
        contentScale = ContentScale.FillWidth
    )

}
package com.sk.shotsapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sk.shotsapp.AppViewModel
import com.sk.shotsapp.R
import com.sk.shotsapp.Screen
import com.sk.shotsapp.ui.theme.ifDarkTheme

@Composable
fun Profile(loginViewModel: AppViewModel, navControllerMain: NavController) {
    Scaffold(topBar = {
        ProfileTopBar(
            navControllerMain = navControllerMain, loginViewModel = loginViewModel
        )
    }) {

        val navController = rememberNavController()
        Column(Modifier.fillMaxSize()) {
            if (loginViewModel.isLoggedIn.value) Avatar(loginViewModel)
            NavigateBetweenScreen(
                navController = navController, loginViewModel = loginViewModel
            )
        }
        print(it)
    }
}

@Composable
fun NavigateBetweenScreen(
    navController: NavHostController,
    loginViewModel: AppViewModel
) {
    val startDestination =
        if (loginViewModel.isLoggedIn.value) "Welcome" else "Login"

    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = "Login") {
            loginViewModel.setError("")
            LoginScreen(
                viewModel = loginViewModel
            )
        }
//        loginPage(this, navController, loginViewModel)
        composable(route = "Sign in with Google") {
            loginViewModel.setError("")
            EmailLoginScreen(loginViewModel)
        }
        composable(route = "Welcome") { WelcomeScreen(loginViewModel) }
    }
}

@Composable
fun WelcomeScreen(viewModel: AppViewModel) {
    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
        Text(
            text = "Welcome ${
                if (FirebaseAuth.getInstance().currentUser?.displayName != null) Firebase.auth.currentUser?.displayName
                else Firebase.auth.currentUser?.email?.dropLast(10)
            }", fontSize = 30.sp,
            textAlign = TextAlign.Center
        )
    }
    viewModel.userName =
        if (FirebaseAuth.getInstance().currentUser?.displayName != null) Firebase.auth.currentUser?.displayName.toString()
        else Firebase.auth.currentUser?.email?.dropLast(10).toString()
}


@Composable
fun ProfileTopBar(navControllerMain: NavController, loginViewModel: AppViewModel) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Title(
            whichScreen = if (loginViewModel.isLoggedIn.value) loginViewModel.userName else Screen.Profile.label
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
            tint = ifDarkTheme(status = false)
        )
    }
}

@Composable
fun Avatar(loginViewModel: AppViewModel) {

    Image(
        painter = if (loginViewModel.isLoggedIn.value && Firebase.auth.currentUser?.photoUrl != null) rememberAsyncImagePainter(
            Firebase.auth.currentUser?.photoUrl
        )
        else painterResource(id = R.drawable.selyn_cat),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        contentDescription = "",
        contentScale = ContentScale.Fit,
        alignment = Alignment.Center
    )

}
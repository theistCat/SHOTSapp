@file:OptIn(ExperimentalMaterialApi::class)

package com.sk.shotsapp.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
import com.sk.shotsapp.ui.theme.BarColor
import com.sk.shotsapp.ui.theme.MyTypography

@Composable
fun Profile(viewModel: AppViewModel, navControllerMain: NavHostController) {
    viewModel.isBottomBarEnabled.value = true
    Scaffold(topBar = {
        ProfileTopBar(
            navControllerMain = navControllerMain, loginViewModel = viewModel
        )
    }) {
        Column(Modifier.fillMaxSize()) {
            viewModel.userName =
                if (FirebaseAuth.getInstance().currentUser?.displayName != null) Firebase.auth.currentUser?.displayName.toString()
                else Firebase.auth.currentUser?.email?.dropLast(10).toString()

            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
                Column(Modifier.fillMaxWidth()) {
                    Avatar(viewModel = viewModel)
                    Text(
                        text = "Interests",
                        fontSize = MyTypography.h4.fontSize,
                        modifier = Modifier.padding(16.dp)
                    )
                    Interests(
                        interests = mutableListOf(
                            "music",
                            "sex",
                            "cooking",
                            "dancing",
                            "fun",
                            "travelling",
                            "art",
                            "coffee",
                            "tea",
                            "sport",
                            "business",
                            "poems",
                            "games"
                        )
                    )
                }
            }
        }
        print(it)
    }
}

//@Composable
//fun NavigateBetweenScreen(
//    loginViewModel: AppViewModel,
//    navControllerMain: NavHostController

//    ) {
//    val startDestination = if (loginViewModel.isLoggedIn.value) "Welcome" else "Login"

//    NavHost(navController = navControllerMain, startDestination = startDestination) {
//        composable(route = "Login") {
//            loginViewModel.setError("")
//            LoginScreen(
//                viewModel = loginViewModel, navControllerMain
//            )
//        }
//        composable(route = "Sign in with Google") {
//            loginViewModel.setError("")
//            EmailLoginScreen(loginViewModel, navControllerMain)
//        }
//        composable(route = "Welcome") { WelcomeScreen(loginViewModel, navControllerMain) }
//        composable("createAccount") { CreateAccount(loginViewModel, navController, navControllerMain) }

//    }
//}

//@Composable
//fun WelcomeScreen(viewModel: AppViewModel, navController: NavController) {
//    viewModel.userName =
//        if (FirebaseAuth.getInstance().currentUser?.displayName != null) Firebase.auth.currentUser?.displayName.toString()
//        else Firebase.auth.currentUser?.email?.dropLast(10).toString()
//
//    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
//        Column(Modifier.fillMaxWidth()) {
//            Avatar(viewModel = viewModel)
//            Text(
//                text = "Interests",
//                fontSize = MyTypography.h4.fontSize,
//                modifier = Modifier.padding(16.dp)
//            )
//            Interests(
//                interests = mutableListOf(
//                    "music",
//                    "sex",
//                    "cooking",
//                    "dancing",
//                    "fun",
//                    "travelling",
//                    "art",
//                    "coffee",
//                    "tea",
//                    "sport",
//                    "business",
//                    "poems",
//                    "games"
//                )
//            )
//        }
//    }
//}

@Composable
fun Interests(interests: List<String>) {
    var isExpanded by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    Card(shape = RoundedCornerShape(25.dp), modifier = Modifier.padding(16.dp)) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .animateContentSize()
                .padding(1.dp)
                .clickable { isExpanded = !isExpanded },
            elevation = 1.dp
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(150.dp), modifier = Modifier
                        .height(if (isExpanded) 400.dp else 150.dp), userScrollEnabled = isExpanded
                ) {
                    items(interests) { interest ->
                        Button(
                            {},
                            shape = RoundedCornerShape(50.dp),
                            modifier = Modifier
                                .padding(8.dp)
                                .widthIn(75.dp),
//                            enabled = false
                        ) {
                            Text(text = interest, maxLines = 1)
                        }
                    }
                }

                Icon(
                    painter = painterResource(id = if (isExpanded) R.drawable.ic_up_arrow else R.drawable.ic_down_arrow),
                    contentDescription = "",
                    modifier = Modifier
                        .clickable(
                            interactionSource = interactionSource, indication = null
                        ) {
                            isExpanded = !isExpanded
                        }
//                        .padding(8.dp),
                        .size(30.dp),
                    tint = Color.Gray
                )
            }
        }
    }

}

@Composable
fun ProfileTopBar(navControllerMain: NavController, loginViewModel: AppViewModel) {
    Box(modifier = Modifier.fillMaxWidth()) {
        SettingsIcon(navControllerMain = navControllerMain)
    }
}

@Composable
fun SettingsIcon(navControllerMain: NavController) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Icon(
            painter = painterResource(id = R.drawable.ic_bars),
            contentDescription = "Profile Icon",
            modifier = Modifier
                .padding(top = 16.dp, end = 16.dp)
                .size(35.dp)
//                .clip(CircleShape)
//                .border(BorderStroke(2.dp, Color.Magenta), shape = CircleShape)
                .align(Alignment.CenterEnd)
                .clickable { navControllerMain.navigate("settings") },
//            tint = ifDarkTheme(status = false)
            tint = BarColor
        )
    }
}

@Composable
fun SettingsBackIcon(navControllerMain: NavController) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Icon(
            painter = painterResource(id = R.drawable.ic_asterisk),
            contentDescription = "Profile Icon",
            modifier = Modifier
                .padding(top = 16.dp, end = 16.dp)
                .size(35.dp)
//                .border(BorderStroke(2.dp, Color.Magenta), shape = CircleShape)
                .align(Alignment.CenterEnd)
                .clickable { navControllerMain.navigate("profile") },
//            tint = ifDarkTheme(status = false)
            tint = BarColor
        )
    }
}

@Composable
fun Avatar(viewModel: AppViewModel) {
    Box(modifier = Modifier.padding(24.dp)) {
        Row {
            Image(
                painter = if (viewModel.isLoggedIn.value && Firebase.auth.currentUser?.photoUrl != null) rememberAsyncImagePainter(
                    Firebase.auth.currentUser?.photoUrl
                )
                else painterResource(id = R.drawable.selyn_cat),
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape),
                contentDescription = "",
                contentScale = ContentScale.Fit,
                alignment = Alignment.Center
            )
            Column {
                Text(
                    text = viewModel.userName,
                    fontSize = MyTypography.h4.fontSize,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "age: ${viewModel.usersAge.value}",
                        fontSize = MyTypography.h5.fontSize,
                        modifier = Modifier.padding(16.dp)
                    )
                    Text(
                        text = "sex: $",
                        fontSize = MyTypography.h5.fontSize,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}
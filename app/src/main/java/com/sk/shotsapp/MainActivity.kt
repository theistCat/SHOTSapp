package com.sk.shotsapp

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sk.shotsapp.screens.EmailLoginScreen
import com.sk.shotsapp.screens.LoginScreen
import com.sk.shotsapp.screens.WelcomeScreen
import com.sk.shotsapp.ui.theme.SHOTScomposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val loginViewModel: AppViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SHOTScomposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NaviG(loginViewModel)
                    loginViewModel.test()
                }
            }
        }
    }
}

@Composable
fun NavigateBetweenScreen(
    navController: NavHostController,
    loginViewModel: AppViewModel
) {
    val startDestination =
        if (loginViewModel.isLoggedIn.value) "Welcome" else "Login"
//        if (loginViewModel.isLoggedIn.value) NavigationEnum.Welcome.name else NavigationEnum.Login.name

    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = "Login") {
            loginViewModel.setError("")
            LoginScreen(
//                emailLoginClick = { navController.navigate("Sign in with Google") },
                viewModel = loginViewModel
            )
        }
//        loginPage(this, navController, loginViewModel)
        composable(route = "Sign in with Google") {
            loginViewModel.setError("")
            EmailLoginScreen(loginViewModel)
        }
//        emailLoginPage(this, loginViewModel)
        composable(route = "Welcome") { WelcomeScreen(loginViewModel) }
//        welcomePage(this, loginViewModel)
    }
}

/*
//@Composable
//fun NavigateBackButton(navController: NavController) {
//    IconButton(onClick = { navController.popBackStack() },
//        modifier = Modifier.semantics { contentDescription = "back button" }) {
//
//        Icon(Icons.Filled.ArrowBack, stringResource(R.string.back_icon))
//    }
//}

//fun loginPage(
//    builder: NavGraphBuilder,
//    navController: NavController,
//    loginViewModel: AppViewModel
//) {
////    builder.composable(route = NavigationEnum.Login.name) {
//    builder.composable(route = "Login") {
//        loginViewModel.setError("")
//        LoginScreen(
//            emailLoginClick = { navController.navigate("Sign in with Google") },
//            viewModel = loginViewModel
//        )
//    }
//}

//fun emailLoginPage(builder: NavGraphBuilder, loginViewModel: AppViewModel) {
//    builder.composable(route = "Sign in with Google") {
//        loginViewModel.setError("")
//        EmailLoginScreen(loginViewModel)
//    }
//}

//fun welcomePage(builder: NavGraphBuilder, loginViewModel: AppViewModel) {
//    builder.composable(route = "Welcome") { WelcomeScreen(loginViewModel) }
//}


 */



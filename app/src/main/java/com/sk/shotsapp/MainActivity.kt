package com.sk.shotsapp

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sk.shotsapp.nav_and_bar.NavigationEnum
import com.sk.shotsapp.screens.EmailLoginScreen
import com.sk.shotsapp.screens.LoginScreen
import com.sk.shotsapp.screens.ProfileScreen
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
                    ProfileScreen()
                    loginViewModel.test()
                }
            }
        }
    }
}

@Composable
fun NavigateBetweenScreen(
    navController: NavHostController,
    loginViewModel: AppViewModel = hiltViewModel()
) {
    val startDestination =
        if (loginViewModel.isLoggedIn.value) NavigationEnum.Welcome.name else NavigationEnum.Login.name

    NavHost(navController = navController, startDestination = startDestination) {
        loginPage(this, navController, loginViewModel)
        emailLoginPage(this, loginViewModel)
        welcomePage(this, loginViewModel)
    }
}

@Composable
fun NavigateBackButton(navController: NavController) {
    IconButton(onClick = { navController.popBackStack() },
        modifier = Modifier.semantics { contentDescription = "back button" }) {

        Icon(Icons.Filled.ArrowBack, stringResource(R.string.back_icon))
    }
}

fun loginPage(
    builder: NavGraphBuilder,
    navController: NavController,
    loginViewModel: AppViewModel
) {
    builder.composable(route = NavigationEnum.Login.name) {
        loginViewModel.setError("")
        LoginScreen(
            emailLoginClick = { navController.navigate(NavigationEnum.EmailLogin.name) },
            viewModel = loginViewModel
        )
    }
}

fun emailLoginPage(builder: NavGraphBuilder, loginViewModel: AppViewModel) {
    builder.composable(route = NavigationEnum.EmailLogin.name) {
        loginViewModel.setError("")
        EmailLoginScreen(loginViewModel)
    }
}

fun welcomePage(builder: NavGraphBuilder, loginViewModel: AppViewModel) {
    builder.composable(route = NavigationEnum.Welcome.name) {
        WelcomeScreen(loginViewModel)
    }
}

// Previews
@Preview(name = "Dark Mode", uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "Light Mode")
@Composable
fun DefaultPreview() {
    SHOTScomposeTheme {

    }
}
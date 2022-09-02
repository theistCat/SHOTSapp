package com.sk.shotsapp.screens

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sk.shotsapp.AppViewModel
import com.sk.shotsapp.R
import com.sk.shotsapp.ui.theme.BarColor
import com.sk.shotsapp.ui.theme.MyTypography
import com.sk.shotsapp.ui.theme.SecondColor

@Composable
fun SettingScreen(viewModel: AppViewModel= hiltViewModel(), navController: NavController) {
    viewModel.isBottomBarEnabled.value = true
    Scaffold(topBar = {
        SettingsBackIcon(navControllerMain = navController)
    }) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.foreground),
                contentDescription = "",
            )
            if (viewModel.isLoggedIn.value) {
                EditProfile(navController = navController)
                LogoutButton(viewModel, navController)
                DeleteUserButton(viewModel, navController)
            }
        }

    }
}

@Composable
fun LogoutButton(viewModel: AppViewModel, navController: NavController) {
    Column(
        modifier = Modifier
//            .fillMaxWidth()
            .padding(top = 40.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                viewModel.signOut()
                navController.navigate("profile")
            }, colors = ButtonDefaults.buttonColors(
                backgroundColor = BarColor, contentColor = Color.White
            ), elevation = ButtonDefaults.elevation(0.dp)
        ) {
            Text(
                text = stringResource(R.string.log_out),
                modifier = Modifier.padding(end = 4.dp),
                fontStyle = MyTypography.h5.fontStyle
            )
            Icon(painter = painterResource(id = R.drawable.ic_exit), contentDescription = null)
        }
    }
}


@Composable
fun DeleteUserButton(viewModel: AppViewModel, navController: NavController) {
    Column(
        modifier = Modifier
//            .fillMaxWidth()
            .padding(top = 40.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                val user = Firebase.auth.currentUser!!

                user.delete().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User account deleted.")
                    }
                }
                viewModel.signOut()
                navController.navigate("profile")
            }, colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Red, contentColor = Color.White
            ), elevation = ButtonDefaults.elevation(0.dp)
        ) {

            Text(
                text = stringResource(R.string.deleteAccount), fontStyle = MyTypography.h5.fontStyle
            )
        }
    }
}

@Composable
fun EditProfile(navController: NavController) {
    Button(
        onClick = {
            navController.navigate("changeAccount")
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = SecondColor, contentColor = Color.White)
    ) {
        Text(
            text = stringResource(id = R.string.changeAccountInfo),
            modifier = Modifier.padding(end = 4.dp),
            fontStyle = MyTypography.h5.fontStyle
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_edit),
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )
    }
}
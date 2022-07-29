package com.sk.shotsapp.screens

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sk.shotsapp.AppViewModel
import com.sk.shotsapp.R
import com.sk.shotsapp.ui.theme.BarColor
import com.sk.shotsapp.ui.theme.ifDarkTheme

@Composable
fun SettingScreen(viewModel: AppViewModel, navController: NavController) {
    Column {
        LogoutButton(viewModel, navController)
        if (viewModel.isLoggedIn.value) {
            DeleteUserButton(viewModel, navController)
        }
    }
}

@Composable
fun LogoutButton(viewModel: AppViewModel, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                viewModel.signOut()
                navController.navigate("profile")
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = BarColor,
                contentColor = Color.White
            )
        ) {
            Text(
                text = if (viewModel.isLoggedIn.value) {
                    stringResource(R.string.log_out)
                } else {
                    "back"
                },
                modifier = Modifier.padding(end = 4.dp),
                fontSize = 20.sp
            )
            Icon(painter = painterResource(id = R.drawable.ic_exit), contentDescription = null)
        }
    }
}


@Composable
fun DeleteUserButton(viewModel: AppViewModel, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                val user = Firebase.auth.currentUser!!

                user.delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "User account deleted.")
                        }
                    }
                viewModel.signOut()
                navController.navigate("profile")
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Red,
                contentColor = ifDarkTheme(true)
            )
        ) {

            Text(
                text = "Delete account",
                modifier = Modifier.padding(end = 4.dp),
                fontSize = 20.sp
            )

//            Icon(painter = painterResource(id = R.drawable.ic_exit), contentDescription = null)
        }
    }
}
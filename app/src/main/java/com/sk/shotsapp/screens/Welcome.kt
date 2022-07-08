package com.sk.shotsapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sk.shotsapp.AppViewModel
import com.sk.shotsapp.R

@Composable
fun WelcomeScreen(viewModel: AppViewModel) {
    Column(
        modifier = Modifier
            .padding(top = 8.dp)
    ) {
        WelcomeText()
//        LogoutButton(viewModel)
    }
}

@Composable
fun WelcomeText() {
    Text(
        text = "${stringResource(R.string.welcome_logged_in)}  " +
                "${
                    if (FirebaseAuth.getInstance().currentUser?.displayName != null)
                        Firebase.auth.currentUser?.displayName
                    else
                        Firebase.auth.currentUser?.email?.dropLast(10)
                }",
        modifier = Modifier
            .wrapContentWidth()
            .padding(8.dp),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h4
    )
}


@Composable
fun LogoutButton(viewModel: AppViewModel, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            viewModel.signOut()
            navController.navigate("profile")
        }) {
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
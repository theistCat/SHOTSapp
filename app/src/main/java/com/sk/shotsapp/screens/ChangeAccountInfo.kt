package com.sk.shotsapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.sk.shotsapp.AppViewModel
import com.sk.shotsapp.R
import com.sk.shotsapp.ui.theme.BarColor
import com.sk.shotsapp.ui.theme.MyTypography

@Composable
fun ChangeAccountInfo(viewModel: AppViewModel, navController: NavController) {
    val user = Firebase.auth.currentUser
    viewModel.isBottomBarEnabled.value = false

    Scaffold(topBar = { Title(whichScreen = "Change Account Info") }) {
        Column {
            DisplayName(viewModel = viewModel)
            Email(viewModel = viewModel)
            PasswordFieldChange(viewModel = viewModel)
            PasswordFieldChangeRetype(viewModel = viewModel)
            DatePickerView(viewModel)
            SaveProfile(viewModel = viewModel, navController = navController, user!!)
        }
    }
}

@Composable
fun Email(viewModel: AppViewModel) {
//    val focusManager = LocalFocusManager.current
//    val userEmail = viewModel.userEmail.value

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = Firebase.auth.currentUser?.email.toString(),
        label = { Text(text = stringResource(R.string.email)) },
        onValueChange = { viewModel.setUserEmail(it) },
        enabled = false,
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            focusedLabelColor = BarColor,
            cursorColor = BarColor,
            focusedIndicatorColor = BarColor,
            backgroundColor = Color.White
        ),
    )
}

@Composable
fun DisplayName(viewModel: AppViewModel) {
//    val focusManager = LocalFocusManager.current
    val usersName = viewModel.usersName.value
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = usersName,
        label = { Text(text = "Name") },
        onValueChange = { viewModel.setUsersName(it) },
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            focusedLabelColor = BarColor,
            cursorColor = BarColor,
            focusedIndicatorColor = BarColor,
            backgroundColor = Color.White
        ),
    )
}


@Composable
fun PasswordFieldChange(viewModel: AppViewModel) {
    val password = viewModel.password.value

    TextField(
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = PasswordVisualTransformation(),
        value = password,
        label = { Text(text = stringResource(R.string.password)) },
        onValueChange = { viewModel.setPassword(it) },
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            focusedLabelColor = BarColor,
            cursorColor = BarColor,
            focusedIndicatorColor = BarColor,
            backgroundColor = Color.White
        )
    )
}

@Composable
fun PasswordFieldChangeRetype(viewModel: AppViewModel) {
    val passwordRetype = viewModel.passwordRetype.value

    TextField(
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = PasswordVisualTransformation(),
        value = passwordRetype,
        label = { Text(text = stringResource(R.string.password_retype)) },
        onValueChange = { viewModel.setPasswordRetype(it) },
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            focusedLabelColor = BarColor,
            cursorColor = BarColor,
            focusedIndicatorColor = BarColor,
            backgroundColor = Color.White
        )
    )
}

@Composable
fun SaveProfile(viewModel: AppViewModel, navController: NavController, user: FirebaseUser) {
    Button(
        onClick = {
            if (viewModel.usersName.value != "") {
                viewModel.isError.value = false
                user.updateProfile(userProfileChangeRequest {
                    displayName = viewModel.usersName.value
                }).addOnSuccessListener { navController.navigate("profile") }
            }
            if (viewModel.password.value == viewModel.passwordRetype.value && viewModel.passwordRetype.value.isNotEmpty() && viewModel.password.value.isNotEmpty()) {
                viewModel.isError.value = false
                user.updatePassword(viewModel.passwordRetype.value)
                    .addOnSuccessListener { navController.navigate("profile") }
            } else viewModel.isError.value = true
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = BarColor, contentColor = Color.White)
    ) {
        Text(
            text = "Change account info",
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
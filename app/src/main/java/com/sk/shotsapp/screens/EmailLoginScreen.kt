package com.sk.shotsapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sk.shotsapp.AppViewModel
import com.sk.shotsapp.R
import com.sk.shotsapp.ui.theme.BarColor
import com.sk.shotsapp.ui.theme.SecondColor

@Composable
fun EmailLoginScreen(
    viewModel: AppViewModel = hiltViewModel()
) {

    Column(
        modifier = Modifier
//            .verticalScroll(rememberScrollState())
            .padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 24.dp)
//            .fillMaxSize(),
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (viewModel.error.value.isNotBlank()) {
            ErrorField(viewModel)
        }
        EmailField(viewModel)
        PasswordField(viewModel)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            ButtonEmailPasswordLogin(viewModel)
            ButtonEmailPasswordCreate(viewModel)
        }
    }
}

@Composable
fun EmailField(viewModel: AppViewModel) {
    val focusManager = LocalFocusManager.current
    val userEmail = viewModel.userEmail.value

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = userEmail,
        label = { Text(text = stringResource(R.string.email)) },
        onValueChange = { viewModel.setUserEmail(it) },
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            focusedLabelColor = BarColor,
            cursorColor = BarColor,
            focusedIndicatorColor = BarColor,
            backgroundColor = Color.White
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(onNext = {
            focusManager.moveFocus(FocusDirection.Down)
        }),
    )
}

@Composable
fun PasswordField(viewModel: AppViewModel) {
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
fun ButtonEmailPasswordLogin(viewModel: AppViewModel) {
    Button(
        enabled = viewModel.isValidEmailAndPassword(),
        content = { Text(text = stringResource(R.string.login), color = Color.White) },
        onClick = {
//            viewModel.fromScreen = "login"
            viewModel.signInWithEmailAndPassword()
//            navController.navigate("changeAccount")
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = BarColor
        )
    )
}

@Composable
fun ButtonEmailPasswordCreate(viewModel: AppViewModel) {
    Button(
        enabled = viewModel.isValidEmailAndPassword(),
        content = { Text(text = stringResource(R.string.create), color = Color.White) },
        onClick = {
//            viewModel.fromScreen = "create"
//            navController.navigate("createAccount")
            viewModel.createUserWithEmailAndPassword()
//            navController.navigate("changeAccount")
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = SecondColor
        )
    )
}
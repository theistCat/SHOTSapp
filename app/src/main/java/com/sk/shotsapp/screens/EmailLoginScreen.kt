package com.sk.shotsapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.sk.shotsapp.AppViewModel
import com.sk.shotsapp.R
import com.sk.shotsapp.ui.theme.ifDarkTheme

@Composable
fun EmailLoginScreen(
    viewModel: AppViewModel
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
        ButtonEmailPasswordLogin(viewModel)
        ButtonEmailPasswordCreate(viewModel)
    }
}

@Composable
fun EmailField(viewModel: AppViewModel) {
    val focusManager = LocalFocusManager.current
    val userEmail = viewModel.userEmail.value

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = userEmail,
        label = { Text(text = stringResource(R.string.email)) },
        onValueChange = { viewModel.setUserEmail(it) },
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedLabelColor = ifDarkTheme(false),
            cursorColor = ifDarkTheme(false),
            focusedBorderColor = ifDarkTheme(status = false)
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

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = PasswordVisualTransformation(),
        value = password,
        label = { Text(text = stringResource(R.string.password)) },
        onValueChange = { viewModel.setPassword(it) },
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedLabelColor = ifDarkTheme(false),
            cursorColor = ifDarkTheme(false),
            focusedBorderColor = ifDarkTheme(status = false)
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
        keyboardActions = KeyboardActions(onSend = {

        })
    )
}

@Composable
fun ButtonEmailPasswordLogin(viewModel: AppViewModel) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        enabled = viewModel.isValidEmailAndPassword(),
        content = { Text(text = stringResource(R.string.login)) },
        onClick = { viewModel.signInWithEmailAndPassword() },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = ifDarkTheme(false), contentColor = ifDarkTheme(true)
        )
    )
}

@Composable
fun ButtonEmailPasswordCreate(viewModel: AppViewModel) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        enabled = viewModel.isValidEmailAndPassword(),
        content = { Text(text = stringResource(R.string.create)) },
        onClick = { viewModel.createUserWithEmailAndPassword() },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = ifDarkTheme(false), contentColor = ifDarkTheme(true)
        )
    )
}
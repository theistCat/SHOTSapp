package com.sk.shotsapp.screens

import android.content.Intent
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.sk.shotsapp.AppViewModel
import com.sk.shotsapp.R
import com.sk.shotsapp.ui.theme.BarColor

private const val TAG = "LoginScreen"

@Composable
fun LoginScreen(
//    emailLoginClick: () -> Unit,
    viewModel: AppViewModel, navController: NavController
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.foreground),
            contentDescription = "",
        )

        Spacer(modifier = Modifier.height(18.dp))

        if (viewModel.error.value.isNotBlank()) {
            ErrorField(viewModel)
        }
        EmailLoginScreen(viewModel = viewModel, navController = navController)
//        SignInWithEmailButton(buttonWidth, emailLoginClick)
        SignInWithGoogleButton(viewModel)
    }

}

@Composable
fun ErrorField(viewModel: AppViewModel) {
    Text(
        text = viewModel.error.value,
        modifier = Modifier.fillMaxWidth(),
        color = Color.Red,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun SignInWithGoogleButton(viewModel: AppViewModel) {
    val context = LocalContext.current
    val token = "613841600443-h95506m95khe6j4amq2pem4os8dua8uj.apps.googleusercontent.com"

    val launcher = registerGoogleActivityResultLauncher(viewModel)

    Button(
        colors = ButtonDefaults.buttonColors(
            backgroundColor = BarColor
        ),
        onClick = {
            val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(token)
                .requestEmail()
                .build()

            val googleSignInClient = GoogleSignIn.getClient(context, signInOptions)
            launcher.launch(googleSignInClient.signInIntent)
        },
    ) {
        SignInButtonRow(
            iconId = R.drawable.ic_google,
            buttonTextId = R.string.sign_in_with_google
        )
    }
}

@Composable
fun registerGoogleActivityResultLauncher(viewModel: AppViewModel): ManagedActivityResultLauncher<Intent, ActivityResult> {
    // Callback
    return rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            viewModel.signInWithGoogleToken(account.idToken!!)
        } catch (e: ApiException) {
            Log.w(TAG, "Google sign in failed", e)
        }
    }
}

@Composable
fun SignInButtonRow(@DrawableRes iconId: Int, @StringRes buttonTextId: Int) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LoginButtonIcon(iconId)
        LoginButtonText(buttonTextId)
    }
}

@Composable
fun LoginButtonIcon(@DrawableRes painterResourceId: Int) {
    Icon(
        tint = Color.White,
        painter = painterResource(painterResourceId),
        contentDescription = null
    )
}

@Composable
fun LoginButtonText(@StringRes stringResourceId: Int) {
    Text(
        text = stringResource(stringResourceId),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.button,
        color = Color.White
    )
}

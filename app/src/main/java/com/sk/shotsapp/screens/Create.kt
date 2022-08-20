package com.sk.shotsapp.screens

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sk.shotsapp.AppViewModel
import com.sk.shotsapp.Screen
import com.sk.shotsapp.ui.theme.BarColor
import com.sk.shotsapp.ui.theme.MyTypography


@Composable
fun CreateNew(viewModel: AppViewModel, navControllerMain: NavController) {

    Scaffold(topBar = { Title(whichScreen = Screen.Create.label) }) {
        val db = Firebase.firestore

        Column(
            Modifier
                .fillMaxSize()
                .padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 24.dp)
        ) {

            NameOfEvent(viewModel = viewModel)
            DescriptionOfEvent(viewModel = viewModel)

            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(24.dp), contentAlignment = Alignment.BottomEnd
            ) {
                Button(
                    onClick = {// Create a new user with a first, middle, and last name

                        if (viewModel.nn.isNotEmpty() && viewModel.dd.isNotEmpty()) {
                            viewModel.isError.value = false
                            val event = hashMapOf(
                                "title" to viewModel.nn,
                                "description" to viewModel.dd,
                                "author" to "${
                                    if (FirebaseAuth.getInstance().currentUser?.displayName != null) Firebase.auth.currentUser?.displayName
                                    else Firebase.auth.currentUser?.email?.dropLast(10)
                                }",
                                "photoUrl" to if (FirebaseAuth.getInstance().currentUser?.photoUrl != null) Firebase.auth.currentUser?.photoUrl
                                else "https://github.com/theistCat/theistCat.github.io/blob/main/icons8-no-image-50.png",
                                "uid" to Firebase.auth.currentUser?.uid
                            )

                            db.collection("events").add(event)
                                .addOnSuccessListener { documentReference ->
                                    Log.d(
                                        TAG,
                                        "DocumentSnapshot added with ID: ${documentReference.id}"
                                    )
                                }.addOnFailureListener { e ->
                                    Log.w(TAG, "Error adding document", e)
                                }

                            navControllerMain.navigate("home")
                        } else {
                            viewModel.isError.value = true
                        }

                    }, colors = ButtonDefaults.buttonColors(
//                        backgroundColor = ifDarkTheme(false), contentColor = ifDarkTheme(true)
                        backgroundColor = BarColor, contentColor = Color.White
                    )
                ) {

                    Text(
                        text = "Add",
                        modifier = Modifier.padding(end = 4.dp),
                        fontStyle = MyTypography.h5.fontStyle
                    )


                }
            }
            print(it)
        }
    }
}

@Composable
fun NameOfEvent(viewModel: AppViewModel) {
    val focusManager = LocalFocusManager.current
    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = { text = it },
        label = { Text(if (!viewModel.isError.value) "title of Event" else "field can't be empty") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        singleLine = true,
        isError = viewModel.isError.value,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = BarColor,
            focusedLabelColor = BarColor,
            cursorColor = BarColor,
            backgroundColor = Color.White
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(onNext = {
            focusManager.moveFocus(FocusDirection.Down)
        }),
    )
    viewModel.nn = text
}

@Composable
fun DescriptionOfEvent(viewModel: AppViewModel) {
    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = { text = it },
        label = { Text(if (!viewModel.isError.value) "description for it" else "field can't be empty") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        isError = viewModel.isError.value,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = BarColor,
            focusedLabelColor = BarColor,
            cursorColor = BarColor,
            backgroundColor = Color.White,
        )
    )
    viewModel.dd = text
}

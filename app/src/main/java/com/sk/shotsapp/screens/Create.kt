package com.sk.shotsapp.screens

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sk.shotsapp.AppViewModel
import com.sk.shotsapp.R
import com.sk.shotsapp.Screen
import com.sk.shotsapp.ui.theme.ifDarkTheme


@Composable
fun CreateNew(viewModel: AppViewModel, navControllerMain: NavController) {
    Scaffold(topBar = { Title(whichScreen = Screen.Create.label) }) {
        val db = Firebase.firestore

        Column(Modifier.fillMaxSize()) {

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
                                }"
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

                            navControllerMain.navigate("events")
                        } else {
                            viewModel.isError.value = true
                        }

                    }, colors = ButtonDefaults.buttonColors(
                        backgroundColor = ifDarkTheme(false), contentColor = ifDarkTheme(true)
                    )
                ) {

                    Text(
                        text = "ADD", modifier = Modifier.padding(end = 4.dp), fontSize = 20.sp
                    )


                }
            }


//            Button(onClick = {
//                db.collection("users")
//                    .get()
//                    .addOnSuccessListener { result ->
//                        for (document in result) {
//                            db.collection("users").document(document.id)
//                                .delete()
//                                .addOnSuccessListener {
//                                    Log.w(TAG, "successfully deleted all documents.")
//                                }
//                                .addOnFailureListener { exception ->
//                                    Log.w(TAG, "Error deleting documents.", exception)
//                                }
//                        }
//                    }
//                    .addOnFailureListener { exception ->
//                        Log.w(TAG, "Error getting documents.", exception)
//                    }
//            }) {
//                Text(text = "DELETE ALL")
//            }

        }

        print(it)
    }
}


@Composable
fun NameOfEvent(viewModel: AppViewModel) {
    var text by remember { mutableStateOf("") }

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text(if (!viewModel.isError.value) "title of Event" else "field can't be empty") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = ifDarkTheme(status = false),
            focusedLabelColor = ifDarkTheme(status = false),
            cursorColor = ifDarkTheme(status = false)
        )
    )
    viewModel.nn = text
}

@Composable
fun DescriptionOfEvent(viewModel: AppViewModel) {
    var text by remember { mutableStateOf("") }

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text(if (!viewModel.isError.value) "description for it" else "field can't be empty") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = ifDarkTheme(status = false),
            focusedLabelColor = ifDarkTheme(status = false),
            cursorColor = ifDarkTheme(status = false)
        )
    )
    viewModel.dd = text
}

package com.sk.shotsapp.screens

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sk.shotsapp.AppViewModel
import com.sk.shotsapp.R
import com.sk.shotsapp.Screen


@Composable
fun CreateNew(viewModel: AppViewModel, navControllerMain: NavController) {
    Scaffold(topBar = { Title(whichScreen = Screen.Create.label) }) {
        val db = Firebase.firestore

        Column() {

            NameOfEvent(viewModel = viewModel)
            DescriptionOfEvent(viewModel = viewModel)

            Button(onClick = {// Create a new user with a first, middle, and last name
                val event = hashMapOf(
                    "title" to viewModel.nn, "description" to viewModel.dd, "author" to "${
                        if (FirebaseAuth.getInstance().currentUser?.displayName != null) Firebase.auth.currentUser?.displayName
                        else Firebase.auth.currentUser?.email?.dropLast(10)
                    }"
                )

                db.collection("events").add(event).addOnSuccessListener { documentReference ->
                    Log.d(
                        TAG, "DocumentSnapshot added with ID: ${documentReference.id}"
                    )
                }.addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }

                navControllerMain.navigate("events")

            }) {
                Text(text = "ADD")

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

    OutlinedTextField(value = text, onValueChange = { text = it }, label = { Text("name") })
    viewModel.nn = text
}

@Composable
fun DescriptionOfEvent(viewModel: AppViewModel) {
    var text by remember { mutableStateOf("") }

    OutlinedTextField(value = text, onValueChange = { text = it }, label = { Text("description") })
    viewModel.dd = text
}

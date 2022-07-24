package com.sk.shotsapp.screens

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sk.shotsapp.Screen

@Preview
@Composable
fun CreateNew() {
    Scaffold(topBar = { Title(whichScreen = Screen.Create.label) }) {
        val db = Firebase.firestore
        Column {
            Button(
                onClick = {// Create a new user with a first, middle, and last name
                    val user = hashMapOf(
                        "name" to "gulyat",
                        "type" to "free",
                        "gde" to "tashkent",
                        "kogda" to 1400
                    )

                    db.collection("users")
                        .add(user)
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                        }
                }) {
                Text(text = "ADD")

            }

            Button(onClick = {
                db.collection("users")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            Log.d(TAG, "${document.id} => ${document.data}")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w(TAG, "Error getting documents.", exception)
                    }
            }) {
                Text(text = "GET")
            }

            Button(onClick = {
                db.collection("users")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            db.collection("users").document(document.id)
                                .delete()
                                .addOnSuccessListener {
                                    Log.w(TAG, "successfully deleted all documents.")
                                }
                                .addOnFailureListener { exception ->
                                    Log.w(TAG, "Error deleting documents.", exception)
                                }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w(TAG, "Error getting documents.", exception)
                    }
            }) {
                Text(text = "DELETE ALL")
            }

        }

        print(it)
    }
}
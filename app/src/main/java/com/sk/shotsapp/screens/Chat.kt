package com.sk.shotsapp.screens

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.sk.shotsapp.Screen

@Composable
fun ChatScreen() {
    Scaffold(topBar = { Title(Screen.Chat.label) }) {

        print(it)
    }
}

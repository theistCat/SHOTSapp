package com.sk.shotsapp.screens

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import com.sk.shotsapp.Screen

@Composable
fun CreateNew() {
    Scaffold(topBar = { Title(whichScreen = Screen.Create.label) }) {

        print(it)
    }
}
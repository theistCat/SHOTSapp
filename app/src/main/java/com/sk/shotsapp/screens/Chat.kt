package com.sk.shotsapp.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sk.shotsapp.Screen

@Composable
fun ChatScreen() {
    Scaffold(topBar = { Title(Screen.Chat.label) }) {

        Text(text = "under construction!", fontSize = 30.sp, modifier = Modifier.padding(16.dp))

        print(it)
    }
}

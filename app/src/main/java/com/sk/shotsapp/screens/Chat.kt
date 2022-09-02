package com.sk.shotsapp.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sk.shotsapp.R

@Composable
fun ChatScreen() {
    Scaffold(topBar = { Title(stringResource(id = R.string.chats)) }) {

        Text(
            text = stringResource(R.string.construction),
            fontSize = 30.sp,
            modifier = Modifier.padding(16.dp)
        )

        print(it)
    }
}

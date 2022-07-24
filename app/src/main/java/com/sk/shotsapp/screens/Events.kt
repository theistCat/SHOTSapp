package com.sk.shotsapp.screens

import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sk.shotsapp.AppViewModel
import com.sk.shotsapp.R
import com.sk.shotsapp.Screen
import kotlinx.coroutines.delay

@Composable
fun EventScreen(
    viewModel: AppViewModel
) {
    Scaffold(topBar = { Title(whichScreen = Screen.Events.label) }) {
        val db = Firebase.firestore

        if (viewModel.doc.isEmpty()) {
            Text(
                text = "Pull to refresh", modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                fontSize = 30.sp
            )
        }

        var refreshing by remember { mutableStateOf(false) }
        LaunchedEffect(refreshing) {
            if (refreshing) {
                delay(2000)
                refreshing = false
            }
        }

        SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing = refreshing), onRefresh = {
            refreshing = true

            viewModel.doc.clear()

            db.collection("events").get().addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                    viewModel.doc.add("${document.id} => ${document.data}")
                }
            }.addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
        }) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(viewModel.doc.size) {
                    Row(Modifier.padding(16.dp)) {
                        Image(
                            painter = painterResource(R.drawable.ic_event),
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                        )

                        Spacer(Modifier.width(8.dp))

                        Text(
                            text = viewModel.doc[it],
                            style = MaterialTheme.typography.subtitle2,
                            modifier = Modifier
                                .weight(1f)
                                .align(Alignment.CenterVertically)
                        )
                    }
                }
            }
        }

        print(it)

    }
}


@Composable
fun Title(whichScreen: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp), contentAlignment = Alignment.Center
    ) {
        Text(text = whichScreen, textAlign = TextAlign.Center, fontSize = 30.sp)
    }
}

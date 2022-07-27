package com.sk.shotsapp.screens

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.sk.shotsapp.ui.theme.ifDarkTheme
import kotlinx.coroutines.delay

@Composable
fun EventScreen(
    viewModel: AppViewModel
) {
    Scaffold(topBar = { Title(whichScreen = Screen.Events.label) }) {
        val db = Firebase.firestore

        if (viewModel.doc.isEmpty()) {
            EmptyMessage()
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
                    viewModel.doc.add("${document["author"]} => ${document["title"]} : ${document["description"]}")
                    viewModel.evetId.add(document.id)
                }
            }.addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
        }) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(viewModel.doc.size) { it ->
                    EventCard(text = viewModel.doc[it], eventId = viewModel.evetId[it])
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
            .background(ifDarkTheme(status = true)),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = whichScreen, textAlign = TextAlign.Center, fontSize = 30.sp)
    }
}

@Composable
fun EventCard(text: String, eventId: String) {
    val db = Firebase.firestore
    val context = LocalContext.current
    Card(border = BorderStroke(2.dp, Color.LightGray)) {
        Row(
            Modifier
                .padding(16.dp)
                .clickable {
                    Toast
                        .makeText(context, text, Toast.LENGTH_SHORT)
                        .show()
                }) {
            Image(
                painter = painterResource(R.drawable.ic_event),
                contentDescription = null,
                modifier = Modifier.size(64.dp),
            )

            Spacer(Modifier.width(8.dp))

            Text(
                text = text,
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )
            Button(
                onClick = {
                    db.collection("events").document(eventId).delete().addOnSuccessListener {
                        Log.w(TAG, "successfully deleted all documents.")

                    }.addOnFailureListener { exception ->
                        Log.w(TAG, "Error deleting documents.", exception)
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = ifDarkTheme(status = false)),
//            modifier = Modifier.padding(4.dp)
            ) {
                Text(text = "X", fontSize = 20.sp, color = ifDarkTheme(status = true))
            }

        }
    }

}

@Composable
fun EmptyMessage() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column() {
            Text(
                text = "Empty",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                fontSize = 50.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = "pull to refresh",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                fontSize = 30.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
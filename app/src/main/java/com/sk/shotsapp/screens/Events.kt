package com.sk.shotsapp.screens

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.sk.shotsapp.AppViewModel
import com.sk.shotsapp.R
import com.sk.shotsapp.ui.theme.BarColor
import com.sk.shotsapp.ui.theme.MyTypography

@Composable
fun Title(whichScreen: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
//            .background(ifDarkTheme(status = true)),
            .background(Color.White),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = whichScreen,
            textAlign = TextAlign.Center,
            fontSize = MyTypography.h5.fontSize,
//            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = BarColor
        )
    }
}

@Composable
fun EventCard(
    text: String,
    eventId: String,
    location: LatLng,
    painter: String,
    viewModel: AppViewModel,
    deleteBtn: Boolean,
    navController: NavController
) {
    val context = LocalContext.current
    Card {
        Row(
            Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .clickable {
                    Toast
                        .makeText(context, text, Toast.LENGTH_SHORT)
                        .show()
                }) {
            Column {
                Image(
                    painter = rememberAsyncImagePainter(model = painter),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)                       // clip to the circle shape
                        .border(2.dp, Color.Gray, CircleShape),
                )

                if (deleteBtn) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = "",
                        tint = BarColor,
                        modifier = Modifier.clickable {
                            viewModel.db.collection("events").document(eventId).delete()
                                .addOnSuccessListener {
                                    Log.w(TAG, "Document successfully deleted.")
                                }.addOnFailureListener { exception ->
                                    Log.w(TAG, "Error deleting document.", exception)
                                }
                            navController.navigate("home")
                        }.padding(8.dp).size(50.dp)
                    )
                }
            }

            Spacer(Modifier.width(8.dp))

            Column {
                Text(
                    text = text,
                    style = MaterialTheme.typography.subtitle2,
                    fontSize = 18.sp
                )
                val cameraPositionState = rememberCameraPositionState()
                cameraPositionState.position = CameraPosition.fromLatLngZoom(
                    LatLng(location.latitude, location.longitude), 12f
                )

                GoogleMap(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    cameraPositionState = cameraPositionState,
                    uiSettings = MapUiSettings(scrollGesturesEnabled = false, zoomGesturesEnabled = false)
                ) {
                    Marker(state = MarkerState(LatLng(location.latitude, location.longitude)))
                }

            }
        }
    }

    Divider(color = BarColor, modifier = Modifier.fillMaxWidth())

}

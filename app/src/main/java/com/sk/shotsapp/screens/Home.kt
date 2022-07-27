@file:OptIn(
    ExperimentalMaterialApi::class
)

package com.sk.shotsapp.screens

import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.maps.android.compose.*
import com.sk.shotsapp.AppViewModel
import com.sk.shotsapp.R
import com.sk.shotsapp.Screen
import com.sk.shotsapp.ui.theme.ifDarkTheme


@Composable
fun HomeScreen(navControllerMain: NavController, viewModel: AppViewModel) {
    val db = Firebase.firestore

    val mapProperties by remember {
        mutableStateOf(
            MapProperties(
                maxZoomPreference = 16f,
                minZoomPreference = 8f,
//                isMyLocationEnabled = true
            )
        )
    }
    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                mapToolbarEnabled = false,
                zoomControlsEnabled = false,
                myLocationButtonEnabled = true
            )
        )
    }
    val tashkent = LatLng(41.2995, 69.2401)
    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(tashkent, 13f)
    }

    Box(Modifier.fillMaxSize()) {
        ////////////////////beginnig of bottomsheet
//        val scope = rememberCoroutineScope()
        val scaffoldState = rememberBottomSheetScaffoldState()
        BottomSheetScaffold(topBar = { Title(whichScreen = Screen.Home.label) },
            sheetContent = {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(50.dp), contentAlignment = Alignment.Center

                ) {
                    Row() {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_up_arrow),
                            contentDescription = "",
//                            modifier = Modifier.size(16.dp)
                        )
//                        Text("Swipe up to expand sheet")
                    }
                }
                /////////////
//                if (viewModel.doc.isEmpty()) {
//                    EmptyMessage()
//                } else {
//                viewModel.doc.clear()
//                viewModel.isReady.value = false
                db.collection("events").get().addOnSuccessListener { result ->
                    viewModel.doc.clear()
                    for (document in result) {
                        viewModel.doc.add("${document["author"]} => ${document["title"]} : ${document["description"]}")
                        viewModel.evetId.add(document.id)
                    }
                    viewModel.isReady.value = true
                }.addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents.", exception)
                }
                if (viewModel.isReady.value) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(viewModel.doc.size) { it ->
                            EventCard(
                                text = viewModel.doc[it], eventId = viewModel.evetId[it]
                            )
                        }
                    }
                }
//                    Button(onClick = {
//                        scope.launch { scaffoldState.bottomSheetState.collapse() }
//                    }) {
//                        Text("Click to collapse sheet")
//                    }
            },
            scaffoldState = scaffoldState,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navControllerMain.navigate(Screen.Create.route) },
                    modifier = Modifier.align(
                        Alignment.BottomEnd
                    ),
                    backgroundColor = ifDarkTheme(true),
                    elevation = FloatingActionButtonDefaults.elevation(2.dp, 4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_create),
                        contentDescription = ""
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.End,
            sheetPeekHeight = 50.dp,
            sheetElevation = 0.dp
        ) { innerPadding ->
            GoogleMap(contentPadding = innerPadding,
                googleMapOptionsFactory = {
                    GoogleMapOptions().mapId("map01").zoomControlsEnabled(false)
                        .mapToolbarEnabled(false).mapType(MapType.NORMAL.value)
                        .camera(cameraPositionState.position)
                },
                properties = mapProperties,
                uiSettings = mapUiSettings,
                cameraPositionState = cameraPositionState,
                onMapLoaded = {

                }) {

            }
        }
    }

}



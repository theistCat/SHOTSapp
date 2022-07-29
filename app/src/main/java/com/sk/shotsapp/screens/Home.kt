@file:OptIn(
    ExperimentalMaterialApi::class, ExperimentalMaterialApi::class
)

package com.sk.shotsapp.screens

import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.maps.android.compose.*
import com.sk.shotsapp.AppViewModel
import com.sk.shotsapp.R
import com.sk.shotsapp.Screen
import com.sk.shotsapp.ui.theme.BarColor
import com.sk.shotsapp.ui.theme.ifDarkTheme
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(navControllerMain: NavController, viewModel: AppViewModel) {
    viewModel.db = Firebase.firestore

    val mapProperties by remember {
        mutableStateOf(
            MapProperties(
                maxZoomPreference = 16f,
                minZoomPreference = 8f,
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
        val scope = rememberCoroutineScope()
        val scaffoldState = rememberBottomSheetScaffoldState()
        val interactionSource = remember { MutableInteractionSource() }
        BottomSheetScaffold(
            topBar = { Title(whichScreen = Screen.Home.label) },
            sheetContent = {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(50.dp), contentAlignment = Alignment.Center

                ) {
                    Row() {
                        Icon(
                            painter = painterResource(
                                id = if (scaffoldState.bottomSheetState.isCollapsed && !scaffoldState.bottomSheetState.isAnimationRunning) R.drawable.ic_up_arrow else if (!scaffoldState.bottomSheetState.isCollapsed && !scaffoldState.bottomSheetState.isAnimationRunning) R.drawable.ic_down_arrow else R.drawable.ic_dash
                            ), contentDescription = "", modifier = Modifier.clickable(
                                interactionSource = interactionSource, indication = null
                            ) {
                                if (scaffoldState.bottomSheetState.isCollapsed) scope.launch { scaffoldState.bottomSheetState.expand() }
                                else scope.launch { scaffoldState.bottomSheetState.collapse() }
                            }, tint = BarColor
                        )
                    }

                }
                viewModel.db.collection("events").get().addOnSuccessListener { result ->
                    viewModel.doc.clear()
                    for (document in result) {
                        viewModel.doc.add("${document["author"]} => ${document["title"]} : ${document["description"]}")
                        viewModel.photoUrl.add(document["photoUrl"].toString())
                        viewModel.evetId.add(document.id)
                        viewModel.uids.add(document["uid"].toString())
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
                                text = viewModel.doc[it],
                                eventId = viewModel.evetId[it],
                                painter = viewModel.photoUrl[it],
                                viewModel,
                                false
                            )
                        }
                    }
                }
            },
            scaffoldState = scaffoldState,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        if (scaffoldState.bottomSheetState.isCollapsed) navControllerMain.navigate(
                            Screen.Create.route
                        )
                    },
                    modifier = Modifier
                        .align(
                            Alignment.BottomEnd
                        )
                        .alpha(if (scaffoldState.bottomSheetState.isCollapsed && !scaffoldState.bottomSheetState.isAnimationRunning) 1f else 0f),
//                    backgroundColor = ifDarkTheme(true),
                    backgroundColor = BarColor,
                    elevation = FloatingActionButtonDefaults.elevation(2.dp, 4.dp),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_create),
                        contentDescription = "",
                        tint = Color.White
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



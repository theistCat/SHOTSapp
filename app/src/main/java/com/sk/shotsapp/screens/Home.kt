@file:OptIn(
    ExperimentalMaterialApi::class, ExperimentalMaterialApi::class
)
@file:Suppress("NAME_SHADOWING", "CAST_NEVER_SUCCEEDS")

package com.sk.shotsapp.screens

import android.app.Activity
import android.content.ContentValues
import android.location.Location
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.maps.android.compose.*
import com.sk.shotsapp.*
import com.sk.shotsapp.R
import com.sk.shotsapp.ui.theme.BarColor
import com.sk.shotsapp.ui.theme.MyTypography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(
    navControllerMain: NavController,
    viewModel: AppViewModel= hiltViewModel(),
    fusedLocationProviderClient: FusedLocationProviderClient
) {
    var currentLocation by remember { mutableStateOf(LocationUtils.getDefaultLocation()) }

    viewModel.db = Firebase.firestore
    val activity = (LocalContext.current as? Activity)
    viewModel.isBottomBarEnabled.value = true

    Box(Modifier.fillMaxSize()) {
        val scope = rememberCoroutineScope()
        val scaffoldState = rememberBottomSheetScaffoldState()
        val interactionSource = remember { MutableInteractionSource() }
        BottomSheetScaffold(
            topBar = { Title(whichScreen = stringResource(id = R.string.home)) },
            sheetContent = {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(50.dp), contentAlignment = Alignment.Center

                ) {
                    Row {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_dash),
                            contentDescription = "",
                            modifier = Modifier
                                .clickable(
                                    interactionSource = interactionSource, indication = null
                                ) {
                                    if (scaffoldState.bottomSheetState.isCollapsed) scope.launch { scaffoldState.bottomSheetState.expand() }
                                    else scope.launch { scaffoldState.bottomSheetState.collapse() }
                                }
                                .offset(y = (-16).dp),
                            tint = Color.Gray
                        )
                    }

                }
//                }
                viewModel.db.collection("events").get().addOnSuccessListener { result ->
                    viewModel.doc.clear()
                    viewModel.uids.clear()
                    viewModel.photoUrl.clear()
                    viewModel.eventId.clear()
                    viewModel.lat.clear()
                    viewModel.lon.clear()
                    for (document in result) {
                        viewModel.doc.add("${document["author"]} => ${document["title"]} : ${document["description"]}")
                        viewModel.photoUrl.add(document["photoUrl"].toString())
                        viewModel.eventId.add(document.id)
                        viewModel.uids.add(document["uid"].toString())
                        viewModel.lat.add(document["lat"] as Double)
                        viewModel.lon.add(document["lon"] as Double)
                    }
                    viewModel.isReady.value = true
                }.addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents.", exception)
                }
                if (viewModel.doc.size == 0) {
                    scope.launch { scaffoldState.bottomSheetState.collapse() }
                }
                if (viewModel.isReady.value && viewModel.doc.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(viewModel.doc.size) {
                            EventCard(
                                text = viewModel.doc[it],
                                eventId = viewModel.eventId[it],
                                painter = viewModel.photoUrl[it],
                                viewModel = viewModel,
                                location = LatLng(viewModel.lat[it], viewModel.lon[it]),
                                deleteBtn = viewModel.uids[it] == Firebase.auth.currentUser?.uid,
                                navController = navControllerMain
                            )
                        }
                    }
                }
                DefaultBackHandler(
                    BackNavElement.default(child = modalBackNavElement(
                        scaffoldState, scope
                    ), handler = { activity?.finish() })
                )

            },
            scaffoldState = scaffoldState,
            floatingActionButton = {
                ExtendedFloatingActionButton(onClick = {
                    if (scaffoldState.bottomSheetState.isCollapsed) navControllerMain.navigate(
                        Screen.Create.route
                    )
                },
                    modifier = Modifier
                        .align(
                            Alignment.BottomEnd
                        )
                        .offset(y = (-50).dp),
                    backgroundColor = Color.White,
                    elevation = FloatingActionButtonDefaults.elevation(2.dp, 4.dp),
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_create_rounded),
                            contentDescription = "",
                            tint = BarColor,
                            modifier = Modifier
//                                .size(20.dp)
                                .padding(4.dp)
                        )
                    },
                    text = {
                        Text(
                            text = stringResource(R.string.addEvent),
                            color = BarColor,
                            fontSize = MyTypography.body1.fontSize
                        )
                    })

            },
            floatingActionButtonPosition = FabPosition.Center,
            sheetPeekHeight = 40.dp,
            sheetElevation = 0.dp, sheetShape = RoundedCornerShape(20.dp)
        ) {
            val cameraPositionState = rememberCameraPositionState()
            cameraPositionState.position = CameraPosition.fromLatLngZoom(
                LocationUtils.getPosition(currentLocation), 12f
            )

            var requestLocationUpdate by remember { mutableStateOf(true) }

            MyGoogleMap(
                currentLocation,
                cameraPositionState,
                onGpsIconClick = {
                    requestLocationUpdate = true
                    Log.w("LOL", "its working")
                }
            )

            if (requestLocationUpdate) {
                LocationPermissionsAndSettingDialogs(
                    updateCurrentLocation = {
                        requestLocationUpdate = false
                        LocationUtils.requestLocationResultCallback(fusedLocationProviderClient) { locationResult ->

                            locationResult.lastLocation?.let { location ->
                                currentLocation = location
                            }

                        }
                    }
                )
            }
        }
    }
}

@Composable
fun MyGoogleMap(
    currentLocation: Location,
    cameraPositionState: CameraPositionState,
    onGpsIconClick: () -> Unit
) {
    Box {
        val mapUiSettings by remember {
            mutableStateOf(
                MapUiSettings(zoomControlsEnabled = false)
            )
        }

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = mapUiSettings,
        ) {
            Marker(
                state = MarkerState(position = LocationUtils.getPosition(currentLocation)),
                title = stringResource(R.string.currnetPosition)
            )
        }

        GpsIconButton(onIconClick = onGpsIconClick)

//        DebugOverlay(cameraPositionState)
    }
}

@Composable
private fun GpsIconButton(onIconClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            FloatingActionButton(
                onClick = onIconClick,
                modifier = Modifier
                    .offset(x = (-10).dp, y = (-200).dp)
                    .size(40.dp),
                backgroundColor = Color.White
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_location),
                    contentDescription = ""
                )
            }
        }
    }
}


//@Composable
//private fun DebugOverlay(
//    cameraPositionState: CameraPositionState,
//) {
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.Bottom,
//    ) {
//        val moving =
//            if (cameraPositionState.isMoving) "moving" else "not moving"
//        Text(
//            text = "Camera is $moving",
//            fontWeight = FontWeight.Bold,
//            color = Color.DarkGray
//        )
//        Text(
//            text = "Camera position is ${cameraPositionState.position}",
//            fontWeight = FontWeight.Bold,
//            color = Color.DarkGray
//        )
//    }
//}


@Composable
fun DefaultBackHandler(backNavElement: BackNavElement) = BackHandler { backNavElement.tryGoBack() }

fun modalBackNavElement(
    state: BottomSheetScaffoldState, coroutineScope: CoroutineScope
) = BackNavElement.needsProcessing {
    if (state.bottomSheetState.isExpanded) {
        coroutineScope.launch { state.bottomSheetState.collapse() }
        BackNavElement.Result.CANNOT_GO_BACK
    } else {
        BackNavElement.Result.CAN_GO_BACK
    }
}



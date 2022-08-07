@file:OptIn(
    ExperimentalMaterialApi::class, ExperimentalMaterialApi::class
)

package com.sk.shotsapp.screens

import android.app.Activity
import android.content.ContentValues
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.maps.android.compose.*
import com.sk.shotsapp.AppViewModel
import com.sk.shotsapp.BackNavElement
import com.sk.shotsapp.R
import com.sk.shotsapp.Screen
import com.sk.shotsapp.ui.theme.BarColor
import com.sk.shotsapp.ui.theme.MyTypography
import com.sk.shotsapp.ui.theme.Shapes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(navControllerMain: NavController, viewModel: AppViewModel) {
    viewModel.db = Firebase.firestore
    val activity = (LocalContext.current as? Activity)

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
//                Card(shape = Shapes.small) {
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
                    for (document in result) {
                        viewModel.doc.add("${document["author"]} => ${document["title"]} : ${document["description"]}")
                        viewModel.photoUrl.add(document["photoUrl"].toString())
                        viewModel.eventId.add(document.id)
                        viewModel.uids.add(document["uid"].toString())
                    }
                    viewModel.isReady.value = true
                }.addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents.", exception)
                }
                if (viewModel.doc.size == 0) {
                    scope.launch { scaffoldState.bottomSheetState.collapse() }
                }
                if (viewModel.isReady.value) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(viewModel.doc.size) {
                            EventCard(
                                text = viewModel.doc[it],
                                eventId = viewModel.eventId[it],
                                painter = viewModel.photoUrl[it],
                                viewModel,
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
//                        .alpha(if (scaffoldState.bottomSheetState.isCollapsed) 1f else 0f),
//                    backgroundColor = ifDarkTheme(true),
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
                            text = "Add Event",
                            color = BarColor,
                            fontSize = MyTypography.body1.fontSize
                        )
                    })

            },
            floatingActionButtonPosition = FabPosition.Center,
            sheetPeekHeight = 40.dp,
            sheetElevation = 0.dp, sheetShape = RoundedCornerShape(20.dp)
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

@Composable
fun DefaultBackHandler(backNavElement: BackNavElement) = BackHandler { backNavElement.tryGoBack() }

//@OptIn(ExperimentalMaterialApi::class)
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



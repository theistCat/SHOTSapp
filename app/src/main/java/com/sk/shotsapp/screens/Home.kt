@file:OptIn(ExperimentalPermissionsApi::class)

package com.sk.shotsapp.screens

import android.Manifest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.sk.shotsapp.R
import com.sk.shotsapp.Screen
import com.sk.shotsapp.ui.theme.Purple200
import com.sk.shotsapp.ui.theme.Purple700


@Composable
fun HomeScreen(navControllerMain: NavController) {

    val permissionState =
        rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)

//    var isMapLoaded by remember { mutableStateOf(false) }

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
        GoogleMap(
            googleMapOptionsFactory = {
                GoogleMapOptions().mapId("map01").zoomControlsEnabled(false)
                    .mapToolbarEnabled(false).mapType(MapType.NORMAL.value)
                    .camera(cameraPositionState.position)
            },
            properties = mapProperties,
            uiSettings = mapUiSettings,
            cameraPositionState = cameraPositionState,
            onMapLoaded = {
//                isMapLoaded = true
                if (permissionState.hasPermission) {
                    mapProperties.isMyLocationEnabled
                } else {
                    permissionState.launchPermissionRequest()
                }
            }
        ) {

        }
//        if (!isMapLoaded) {
//            AnimatedVisibility(
//                modifier = Modifier.matchParentSize(),
//                visible = !isMapLoaded,
//                enter = EnterTransition.None,
//                exit = fadeOut()
//            ) {
//                CircularProgressIndicator(
//                    modifier = Modifier
//                        .background(MaterialTheme.colors.background)
//                        .wrapContentSize()
//                )
//
//            }
//        }
        FloatingActionButton(
            onClick = { navControllerMain.navigate(Screen.Create.route) },
            modifier = Modifier
                .align(
                    Alignment.BottomEnd
                )
                .offset((-16).dp, (-16).dp),
            backgroundColor = Color.White,

            ) {
            Icon(painter = painterResource(id = R.drawable.ic_create), contentDescription = "")
        }
    }

}



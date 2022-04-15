package com.sk.shotsapp.nav_and_bar

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sk.shotsapp.R

@Composable
fun TopBar(navController: NavController) {
    var isExpanded by remember { mutableStateOf(true) }
    Surface(
        Modifier
            .padding(4.dp)
            .animateContentSize()
//            .clip(CircleShape)
            .fillMaxWidth(),
        color = Color.Transparent
    ) {
        Row(modifier = Modifier.fillMaxWidth(if (isExpanded) 0.1f else 1f)) {
            if (!isExpanded) {
                Image(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(4.dp)
                        .clickable(indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = { isExpanded = !isExpanded })
                )
                Column {
                    Text(
                        text = "Sam Collins",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 32.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp)
                            .wrapContentSize(align = Alignment.Center)
                            .offset(x = (-8).dp)
                            .clickable {
                                isExpanded = !isExpanded
                                navController.navigate("profile") {
                                    navController.graph.startDestinationRoute?.let { route ->
                                        popUpTo(route) {
                                            saveState = true
                                        }
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                    )
                    Divider(modifier = Modifier.padding(4.dp))
                    Text(
                        text = "Settings",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 32.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp)
                            .wrapContentSize(align = Alignment.Center)
                            .offset(x = (-8).dp)
                            .clickable {
                                isExpanded = !isExpanded
                                navController.navigate("settings") {
                                    navController.graph.startDestinationRoute?.let { route ->
                                        popUpTo(route) {
                                            saveState = true
                                        }
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                    )
                }
            }
            Image(
                painter = painterResource(id = R.drawable.ic_menu),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable(indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { isExpanded = !isExpanded }
            )
//                    .border(1.5.dp, MaterialTheme.colors.error, CircleShape)
            Image(
                painter = painterResource(id = R.drawable.ic_notifications),
                contentDescription = "notifications",
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .clickable(indication = null,
                        interactionSource = remember { MutableInteractionSource() }) {  }
                    .wrapContentSize(Alignment.CenterEnd)
                    .alpha(1f)
            )
        }
    }
}
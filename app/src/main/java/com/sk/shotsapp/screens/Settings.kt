package com.sk.shotsapp.screens

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.sk.shotsapp.AppViewModel

@Composable
fun SettingScreen(viewModel: AppViewModel, navController: NavController) {
    LogoutButton(viewModel, navController)
}
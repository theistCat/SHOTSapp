package com.sk.shotsapp

import androidx.annotation.DrawableRes

sealed class Screen(val route: String, @DrawableRes val resourceId: Int, val label: String) {
    object Home : Screen("home", R.drawable.ic_home, "Home")
    object Events : Screen("events", R.drawable.ic_event, "Events")
}

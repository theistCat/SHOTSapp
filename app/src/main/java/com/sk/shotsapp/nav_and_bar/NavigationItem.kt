package com.sk.shotsapp.nav_and_bar

import com.sk.shotsapp.R

sealed class NavigationItem(var route: String, var icon: Int, var title: String) {
    object Home : NavigationItem("home", R.drawable.ic_home, "Home")
    object Map : NavigationItem("map", R.drawable.ic_map, "Map")
    object Event : NavigationItem("events", R.drawable.ic_event, "Events")
}
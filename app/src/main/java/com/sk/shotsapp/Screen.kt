package com.sk.shotsapp

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

sealed class Screen(val route: String, @DrawableRes val resourceId: Int, @StringRes val label: Int) {
    object Home : Screen("home", R.drawable.ic_home, R.string.home)
    object Chat : Screen("chat", R.drawable.ic_chat, R.string.chats)
    object Create : Screen("create", R.drawable.ic_create, R.string.createLabel)
    object Profile : Screen("profile", R.drawable.ic_profile, R.string.profile)
}

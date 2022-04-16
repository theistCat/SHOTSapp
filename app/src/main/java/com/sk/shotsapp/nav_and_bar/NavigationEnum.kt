package com.sk.shotsapp.nav_and_bar

import androidx.compose.runtime.State
import com.sk.shotsapp.R

enum class NavigationEnum constructor(val title: Int) {
    Login(
        title = R.string.login
    ),
    EmailLogin(
        title = R.string.sign_in_with_email
    ),
    Welcome(
        title = R.string.welcome
    );

    companion object {
        fun fromRoute(route: String?, isLoggedIn: State<Boolean>): NavigationEnum {
            return if (!isLoggedIn.value) {
                when (route?.substringBefore("/")) {
                    Login.name -> Login
                    EmailLogin.name -> EmailLogin
                    else -> Login // Redirects to Login if some other page, but not logged in
                }
            } else {
                // Define here all your logged in routings
                when (route?.substringBefore("/")) {
                    Welcome.name -> Welcome
                    Login.name -> Welcome
                    EmailLogin.name -> Welcome
                    null -> Welcome
                    else -> throw IllegalArgumentException("Route $route is not recognized.")
                }
            }
        }
    }
}
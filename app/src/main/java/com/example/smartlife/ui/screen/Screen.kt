package com.example.smartlife.ui.screen

sealed class Screen(val route: String){
    object Home: Screen("home")
    object Todo: Screen("todo")
    object Journal: Screen("journal")
    object Profile: Screen("profile")
}
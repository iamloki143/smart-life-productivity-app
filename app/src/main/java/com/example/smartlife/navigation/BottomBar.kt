package com.example.smartlife.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.smartlife.ui.screen.Screen

@Composable
fun BottomBar(navController: NavHostController) {
    val items=listOf(
        Screen.Home,
        Screen.Todo,
        Screen.Journal,
        Screen.Profile
    )
    NavigationBar {
        val currentRoute=navController.currentBackStackEntryAsState().value?.destination?.route

        items.forEach {screen ->
            NavigationBarItem(
                selected = currentRoute==screen.route,
                onClick = {
                    navController.navigate(screen.route){
                        popUpTo("home")
                        launchSingleTop=true
                    }
                },
                icon = {
                    Icon(
                        imageVector = when(screen){
                            Screen.Home -> Icons.Default.Home
                            Screen.Todo -> Icons.Default.List
                            Screen.Journal -> Icons.Default.DateRange
                            Screen.Profile -> Icons.Default.Person
                        },
                        contentDescription = screen.route
                    )
                },
                label = {
                    Text(screen.route)
                }
            )
        }
    }
}
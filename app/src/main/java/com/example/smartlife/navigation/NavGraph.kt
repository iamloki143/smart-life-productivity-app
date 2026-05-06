package com.example.smartlife.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.internal.composableLambda
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartlife.ui.screen.Screen
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import com.example.smartlife.ui.screen.home.HomeScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.smartlife.data.reposatery.DatabaseProvider
import com.example.smartlife.ui.screen.journal.JournalScreen
import com.example.smartlife.ui.screen.profile.ProfileScreen
import com.example.smartlife.ui.screen.todo.TodoScreen
import com.example.smartlife.viewmodel.journalviewmodel.JournalViewModel
import com.example.smartlife.viewmodel.journalviewmodel.JournalViewModelFactory
import com.example.smartlife.viewmodel.todoViewModel.TodoViewModel
import com.example.smartlife.viewmodel.todoViewModel.TodoViewModelFactory

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController=navController,
        startDestination=Screen.Home.route
    ){
        composable(Screen.Home.route){
            HomeScreen()
        }
        composable(Screen.Todo.route){
            val db= DatabaseProvider.getDB(LocalContext.current)
            val dao=db.todoDao()
            val viewModel: TodoViewModel = viewModel(
                factory = TodoViewModelFactory(dao)
            )
            TodoScreen(viewModel=viewModel)
        }
        composable(Screen.Journal.route){
            val db= DatabaseProvider.getDB((LocalContext.current))
            val dao=db.journalDao()
            val viewModel: JournalViewModel=viewModel(
                factory = JournalViewModelFactory(dao)
            )
            JournalScreen(viewModel=viewModel)
        }
        composable(Screen.Profile.route){
            ProfileScreen()
        }
    }
}
package com.example.carlosmarket.navigate

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.carlosmarket.firebase.data.DataBaseViewModel
import com.example.carlosmarket.screens.MainAddClasses
import com.example.carlosmarket.screens.MainAddStudent
import com.example.carlosmarket.screens.MainClassesHub
import com.example.carlosmarket.screens.MainLoginOrRegister
import com.example.carlosmarket.screens.MainMyClasses

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppScreen.LoginOrRegister.route
    ){
        composable(AppScreen.LoginOrRegister.route) {
            MainLoginOrRegister(navController = navController, viewModel = DataBaseViewModel())
        }
        composable(AppScreen.AddClasses.route) {
            MainAddClasses(navController = navController)
        }
        composable(AppScreen.AddStudents.route) {
            MainAddStudent()
        }
        composable(AppScreen.ClassesHub.route) {
            MainClassesHub(navController = navController)
        }
        composable(
            route = AppScreen.MyClasses.route,
            arguments = listOf(navArgument("classesId") { type = NavType.StringType })
        ) {backStackEntry ->
            val classesId = backStackEntry.arguments?.getString("classesId")

            if (classesId != null) {
                MainMyClasses(classesId = classesId,navController = navController)
            }
        }
    }
}
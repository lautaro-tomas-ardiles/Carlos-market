package com.example.carlosmarket.navigate

sealed class AppScreen(val route: String) {
    data object LoginOrRegister : AppScreen("login_or_register")
    data object AddClasses : AppScreen("add_classes")
    data object AddStudents : AppScreen("add_students")
    data object ClassesHub : AppScreen("classes_hub")
    data object MyClasses : AppScreen("my_classes/{classesId}") {
        fun createRoute(classesId: String) = "my_classes/$classesId"
    }
}
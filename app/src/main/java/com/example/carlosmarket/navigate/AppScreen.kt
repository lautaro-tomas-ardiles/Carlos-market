package com.example.carlosmarket.navigate

sealed class AppScreen(val route: String) {
    object LoginOrRegister : AppScreen("login_or_register")
    object AddClasses : AppScreen("add_classes")
    object AddStudents : AppScreen("add_students")
    object ClassesHub : AppScreen("classes_hub")
    object MyClasses : AppScreen("my_classes/{classesId}") {
        fun createRoute(classesId: String) = "my_classes/$classesId"
    }
}
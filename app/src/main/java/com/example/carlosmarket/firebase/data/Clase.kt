package com.example.carlosmarket.firebase.data

data class Clase(
    val id: String,
    val nombre: String,
    val profesorId: String,
    val items: List<String> // nueva propiedad opcional
)


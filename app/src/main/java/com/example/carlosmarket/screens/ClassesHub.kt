package com.example.carlosmarket.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.carlosmarket.firebase.data.Clase
import com.example.carlosmarket.firebase.data.FireBaseCRUD
import com.example.carlosmarket.navigate.AppScreen
import com.example.carlosmarket.ui.theme.DarkBlue
import com.example.carlosmarket.ui.theme.Green
import com.example.carlosmarket.utilities.BottomBar
import com.example.carlosmarket.utilities.FloatButton
import com.example.carlosmarket.utilities.TextAndDivider
import com.example.carlosmarket.utilities.TopBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration

@Composable
private fun Classes(
    clase: Clase,
    deleteState: Boolean,
    navController: NavController,
    onDelete: (Clase) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Green)
            .border(
                width = 4.dp,
                brush = if (deleteState) Brush.horizontalGradient(
                    listOf(
                        Color.Yellow,
                        Color.Red
                    )
                ) else Brush.horizontalGradient(
                    listOf(Color.Transparent, Color.Transparent)
                ),
                shape = RectangleShape
            )
            .clickable {
                if (deleteState) {
                    onDelete(clase)
                } else {
                    navController.navigate(AppScreen.MyClasses.createRoute(classesId = clase.id))
                }
            }
            .padding(vertical = 15.dp, horizontal = 15.dp)
    ) {
        Text(clase.nombre)
    }
    Spacer(modifier = Modifier.height(1.dp))
}

@Composable
private fun ListOfClasses(
    deleteState: Boolean,
    navController: NavController
) {
    val clases = remember { mutableStateListOf<Clase>() }
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    // Actualiza lista en tiempo real
    DisposableEffect(uid) {
        var listener: ListenerRegistration? = null
        uid?.let {
            listener = FireBaseCRUD().getClasesDelProfesor(
                uid = it,
                onResult = { lista ->
                    clases.clear()
                    clases.addAll(lista)
                }
            )
        }

        onDispose {
            listener?.remove()
        }
    }

    LazyColumn {
        items(clases, key = { it.id }) { clase ->
            Classes(
                clase = clase,
                deleteState = deleteState,
                navController = navController,
                onDelete = { selectedClase ->
                    FireBaseCRUD().deleteClaseById(selectedClase.id)
                    clases.remove(selectedClase) // Optimista, luego se actualiza vía listener
                }
            )
        }
    }
}

@Composable
fun MainClassesHub(navController: NavController) {
    var deleteState by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopBar(
                topBarButton = false,
                topBarOnClick = { /*TODO*/ }
            )
        },
        bottomBar = {
            BottomBar(
                deleteOnClick = { deleteState = !deleteState }
            )
        },
        floatingActionButton = {
            FloatButton(
                floatOnClick = { navController.navigate(AppScreen.AddClasses.route) }
            )
        },
        containerColor = DarkBlue,
        floatingActionButtonPosition = FabPosition.EndOverlay
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            TextAndDivider("Mis clases")

            ListOfClasses(deleteState = deleteState, navController)
        }
    }
}
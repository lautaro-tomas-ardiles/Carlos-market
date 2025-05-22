package com.example.carlosmarket.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.carlosmarket.firebase.data.FireBaseCRUD
import com.example.carlosmarket.navigate.AppScreen
import com.example.carlosmarket.ui.theme.DarkBlue
import com.example.carlosmarket.ui.theme.LightBlue
import com.example.carlosmarket.ui.theme.Red
import com.example.carlosmarket.ui.theme.Yellow
import com.example.carlosmarket.utilities.TopBar
import com.google.firebase.auth.FirebaseAuth

@Composable
fun TextsFieldLoginOrRegister(
    name: String,
    isNameInvalid: Boolean,
    onNameChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Ingrese el nombre de la clase") },
            isError = isNameInvalid,
            supportingText = {
                if (isNameInvalid) Text("El nombre no puede estar vacío")
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Yellow,
                focusedLabelColor = Yellow,
                unfocusedBorderColor = LightBlue,
                focusedTextColor = Yellow,
                unfocusedTextColor = Color.White,
                errorBorderColor = Red,
                errorLabelColor = Red
            ),
            modifier = Modifier.fillMaxWidth(0.75f)
        )
    }
}

@Composable
fun ButtonsAddClasses(
    name: String,
    onInvalidName: (Boolean) -> Unit,
    navController: NavController
) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                val nameError = name.isBlank()
                val uid = FirebaseAuth.getInstance().currentUser?.uid
                onInvalidName(nameError)

                if (!nameError) {
                    val dataClass = hashMapOf(
                        "nombre" to name,
                        "profesorId" to uid,
                        "items" to listOf("pepa","pepe")
                    )

                    FireBaseCRUD().addClass(dataClass)
                }
                navController.navigate(AppScreen.ClassesHub.route)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = LightBlue,
                contentColor = Color.Black
            )
        ) {
            Text("Agregar")
        }
    }
}

@Composable
fun MainAddClasses(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var isNameInvalid by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopBar(
                topBarOnClick = { navController.popBackStack() },
                topBarButton = true
            )
        },
        containerColor = DarkBlue
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(15.dp))

            Text(
                text = "Agregar clases",
                color = Color.White
            )

            TextsFieldLoginOrRegister(
                name = name,
                isNameInvalid = isNameInvalid,
                onNameChange = {
                    name = it
                    isNameInvalid = false
                },
            )

            ButtonsAddClasses(
                name = name,
                onInvalidName = { isNameInvalid = it },
                navController = navController
            )
        }
    }
}

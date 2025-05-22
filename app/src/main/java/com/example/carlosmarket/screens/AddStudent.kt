package com.example.carlosmarket.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.carlosmarket.ui.theme.DarkBlue
import com.example.carlosmarket.ui.theme.LightBlue
import com.example.carlosmarket.ui.theme.Yellow
import com.example.carlosmarket.utilities.TopBar

//alumnos que existen es temporal despues se cambiara por una base de datos
val students = listOf(
    "Sofía Ramírez",
    "Tomás Herrera",
    "Valentina Gómez",
    "Lucas Mendoza",
    "Camila Torres",
    "juan"
)

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextsFieldAddStudent() {
    var studentName by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var studentsList by remember { mutableStateOf(mutableListOf<String>()) }
    var query by remember { mutableStateOf("") }

    // Filtra la lista de estudiantes según el texto introducido
    val filteredStudents = students.filter { it.contains(query, ignoreCase = true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = studentName,
                onValueChange = { studentName = it },
                label = { Text("Nombre del alumno") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                leadingIcon = {
                    // Ícono de búsqueda que filtra la lista de estudiantes
                    IconButton(
                        onClick = {
                            // Actualiza el estado de la consulta (query) para activar el filtro
                            query = studentName
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = Yellow
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Yellow,
                    focusedLabelColor = Yellow,
                    unfocusedBorderColor = LightBlue,
                    focusedTextColor = Yellow,
                    unfocusedTextColor = Color.White
                ),
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                filteredStudents.forEach { student ->
                    DropdownMenuItem(
                        text = { Text(student) },
                        onClick = {
                            studentName = student
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.padding(10.dp))

        Button(
            onClick = {
                if (studentName.isNotEmpty() && !studentsList.contains(studentName)) {
                    studentsList = studentsList.toMutableList().apply { add(studentName) }
                    studentName = ""
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = LightBlue,
                contentColor = Color.Black
            )
        ) {
            Text("Agregar alumno")
        }

        Spacer(modifier = Modifier.padding(10.dp))

        Text(
            text = "Lista de alumnos :",
            color = Color.White
        )

        Spacer(modifier = Modifier.padding(10.dp))

        LazyColumn {
            items(studentsList) { item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item,
                        color = Yellow
                    )
                    Spacer(modifier = Modifier.padding(2.dp))

                    IconButton(
                        onClick = {
                            studentsList = studentsList.toMutableList().apply { remove(item) }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Yellow
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.padding(10.dp))

        Button(
            onClick = {/*TODO*/},
            colors = ButtonDefaults.buttonColors(
                containerColor = Yellow,
                contentColor = Color.Black
            )
        ) {
            Text("Guardar cambios")
        }
    }
}

@Composable
fun MainAddStudent() {
    Scaffold (
        topBar = {
            TopBar(
                topBarButton = false,
                topBarOnClick = { /*TODO*/ }
            )
        },
        containerColor = DarkBlue
    ) {paddingValues ->
        Column (
            modifier = Modifier.padding(paddingValues)
        ) {
            TextsFieldAddStudent()
        }
    }
}

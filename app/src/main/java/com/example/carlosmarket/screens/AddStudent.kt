@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.carlosmarket.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.runtime.mutableStateListOf
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
import com.example.carlosmarket.utilities.Buttons
import com.example.carlosmarket.utilities.TopBar

//alumnos que existen es temporal despues se cambiara por una base de datos
private val students = listOf(
    "Sofía Ramírez",
    "Tomás Herrera",
    "Valentina Gómez",
    "Lucas Mendoza",
    "Camila Torres",
    "juan"
)

@Composable
private fun StudentDropdown(
    studentName: String,
    onNameChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    filteredStudents: List<String>,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = studentName,
            onValueChange = onNameChange,
            label = {
                Text("Nombre del alumno")
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            leadingIcon = {
                IconButton(
                    onClick = { onSearch(studentName) }
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
                        onSelect(student)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun StudentList(
    studentsList: List<String>,
    onRemove: (String) -> Unit
) {
    LazyColumn {
        items(studentsList) { student ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(student, color = Yellow)

                IconButton(onClick = { onRemove(student) }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Eliminar",
                        tint = Yellow
                    )
                }
            }
        }
    }
}

@Composable
fun MainAddStudent() {
    var studentName by remember { mutableStateOf("") }
    var query by remember { mutableStateOf("") }
    val studentsList = remember { mutableStateListOf<String>() }

    val filteredStudents = students.filter { it.contains(query, ignoreCase = true) }

    Scaffold(
        topBar = {
            TopBar(
                topBarButton = false,
                topBarOnClick = { /* TODO */ }
            )
        },
        containerColor = DarkBlue
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Dropdown de búsqueda
            StudentDropdown(
                studentName = studentName,
                onNameChange = { studentName = it },
                onSearch = { query = it },
                filteredStudents = filteredStudents,
                onSelect = { studentName = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para agregar alumno
            Buttons("Agregar alumno") {
                if (studentName.isNotBlank() && !studentsList.contains(studentName)) {
                    studentsList.add(studentName)
                    studentName = ""
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Lista de alumnos:",
                color = Yellow
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Lista de alumnos
            StudentList(
                studentsList = studentsList,
                onRemove = { studentsList.remove(it) }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Buttons("Guardar cambios") {
                // TODO: Lógica para guardar
            }
        }
    }
}
@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.carlosmarket.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.carlosmarket.R
import com.example.carlosmarket.firebase.data.FireBaseCRUD
import com.example.carlosmarket.ui.theme.DarkBlue
import com.example.carlosmarket.ui.theme.Green
import com.example.carlosmarket.ui.theme.LightBlue
import com.example.carlosmarket.ui.theme.Yellow
import com.example.carlosmarket.utilities.BottomBar
import com.example.carlosmarket.utilities.FloatButton
import com.example.carlosmarket.utilities.TextAndDivider
import com.example.carlosmarket.utilities.TopBar

@Composable
private fun SearchBarContent(
    query: MutableState<String>,
    active: MutableState<Boolean>,
    filtered: List<String>,
    onStudentSelected: (String?) -> Unit,
    leadingIconClick: () -> Unit,
    trailingIconClick: () -> Unit
) {
    SearchBar(
        query = query.value,
        onQueryChange = { query.value = it },
        onSearch = { active.value = false },
        active = active.value,
        onActiveChange = { active.value = it },
        placeholder = { Text("Buscar personas...") },
        leadingIcon = {
            IconButton(
                onClick = {
                    leadingIconClick()
                }
            ) {
                Icon(
                    imageVector = if (active.value) Icons.Default.Menu else Icons.Default.Close,
                    contentDescription = if (active.value) "Cerrar búsqueda" else "Abrir búsqueda"
                )
            }
        },
        trailingIcon = {
            IconButton(
                onClick = {
                    trailingIconClick()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Limpiar búsqueda"
                )
            }
        },
        colors = SearchBarDefaults.colors(
            containerColor = LightBlue,
            dividerColor = DarkBlue,
            inputFieldColors = SearchBarDefaults.inputFieldColors(
                unfocusedTextColor = Color.Black,
                unfocusedPlaceholderColor = Color.Black,
                unfocusedLeadingIconColor = Color.Black,
                unfocusedTrailingIconColor = Color.Black,
                focusedTrailingIconColor = Color.Black,
                focusedLeadingIconColor = Color.Black,
                focusedTextColor = Color.Black,
                focusedPlaceholderColor = Color.Black,
                selectionColors = TextSelectionColors(
                    handleColor = Yellow,
                    backgroundColor = Yellow
                )
            )
        )
    ) {
        filtered.forEach { name ->
            ListItem(
                headlineContent = { Text(name) },
                colors = ListItemDefaults.colors(
                    containerColor = LightBlue,
                    headlineColor = Color.Black,
                    overlineColor = Color.Black
                ),
                modifier = Modifier.clickable {
                    query.value = name
                    active.value = false
                    onStudentSelected(name)
                }
            )
        }
    }
}

@Composable
private fun Searchbars(
    students: List<String>,
    onStudentSelected: (String?) -> Unit
) {
    val query = remember { mutableStateOf("") }
    val active = remember { mutableStateOf(false) }

    LaunchedEffect(query.value) {
        if (query.value.isEmpty()) {
            onStudentSelected(null)
        }
    }

    val filtered = students.filter { it.contains(query.value, ignoreCase = true) }

    SearchBarContent(
        query = query,
        active = active,
        filtered = filtered,
        onStudentSelected = onStudentSelected,
        leadingIconClick = {
            query.value = ""
            active.value = !active.value
            if (!active.value) onStudentSelected(null)
        },
        trailingIconClick = {
            query.value = ""
            onStudentSelected(null)
        }
    )
}

@Composable
private fun Student(
    name: String,
    balance: Int
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(Green)
            .fillMaxWidth()
            .padding(
                vertical = 5.dp,
                horizontal = 10.dp
            )
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
        ) {
            Text(
                text = name,
                fontSize = 17.sp
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = { /*TODO*/ },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color.Black,
                    containerColor = Yellow
                ),
                modifier = Modifier.size(40.dp)
            )  {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
            Spacer(modifier = Modifier.padding(3.dp))

            Text("$$balance")

            Spacer(modifier = Modifier.padding(3.dp))

            IconButton(
                onClick = { /*TODO*/ },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color.Black,
                    containerColor = Yellow
                ),
                modifier = Modifier.size(40.dp)
            )  {
                Icon(
                    painter = painterResource(R.drawable.icon),
                    contentDescription = "Minus"
                )
            }
        }
    }
    HorizontalDivider()
}

@Composable
private fun ListsOfStudents(
    selectedStudent: String?,
    alumnos: List<String>
) {
    if (!selectedStudent.isNullOrBlank()) {
        Student(selectedStudent, 100)
    } else {
        LazyColumn {
            items(alumnos) { student ->
                Student(student, 100)
            }
        }
    }
}

@Composable
fun MainMyClasses(navController: NavController, classesId: String) { // <- Debe ser String, no Int
    var selectedStudent by remember { mutableStateOf<String?>(null) }
    var alumnos by remember { mutableStateOf<List<String>>(emptyList()) }

    // Obtener datos desde Firestore
    LaunchedEffect(classesId) {
        FireBaseCRUD().getClaseById(
            claseId = classesId,
            onSuccess = { clase ->
                alumnos = clase.items // ← usamos `items`, porque así se llama en tu función
            },
            onFailure = { exception ->
                Log.e("MainMyClasses", "Error al obtener clase: ${exception.message}")
            }
        )
    }

    Scaffold(
        topBar = {
            TopBar(
                topBarOnClick = { navController.popBackStack() },
                topBarButton = true
            )
        },
        bottomBar = {
            BottomBar(deleteOnClick = {/*TODO*/ })
        },
        floatingActionButton = {
            FloatButton(floatOnClick = {/*TODO*/ })
        },
        containerColor = DarkBlue,
        floatingActionButtonPosition = FabPosition.EndOverlay
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(paddingValues)
        ) {
            Searchbars(
                students = alumnos,
                onStudentSelected = { selectedStudent = it }
            )

            TextAndDivider("Alumnos")

            ListsOfStudents(selectedStudent, alumnos)
        }
    }
}

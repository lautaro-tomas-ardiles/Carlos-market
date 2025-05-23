package com.example.carlosmarket.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.carlosmarket.R
import com.example.carlosmarket.firebase.data.DataBaseViewModel
import com.example.carlosmarket.firebase.data.FireBaseCRUD
import com.example.carlosmarket.navigate.AppScreen
import com.example.carlosmarket.ui.theme.DarkBlue
import com.example.carlosmarket.ui.theme.LightBlue
import com.example.carlosmarket.ui.theme.Red
import com.example.carlosmarket.ui.theme.Yellow
import com.example.carlosmarket.utilities.Buttons
import com.example.carlosmarket.utilities.TopBar
import com.google.firebase.auth.FirebaseAuth

@Composable
fun TextsField(
    valueOnChanged: (String) -> Unit,
    label: String,
    isPassword: Boolean = false,
    isEmail: Boolean = false
) {
    var value by remember { mutableStateOf("") }

    var passwordVisibility by remember { mutableStateOf(true) }

    OutlinedTextField(
        value = value,
        onValueChange = {
            value = it
            valueOnChanged(it)
        },
        label = {
            Text(label)
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
        trailingIcon = {
            if (isPassword) {
                IconButton(
                    onClick = { passwordVisibility = !passwordVisibility }
                ) {
                    Icon(
                        painter = if (passwordVisibility) painterResource(R.drawable.eye)
                        else painterResource(R.drawable.eye_off),
                        contentDescription = "show password",
                        tint = LightBlue,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        },
        visualTransformation =
        if (!passwordVisibility) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        keyboardOptions = when {
            isEmail -> KeyboardOptions(keyboardType = KeyboardType.Email)
            isPassword -> KeyboardOptions(keyboardType = KeyboardType.Password)
            else -> KeyboardOptions(keyboardType = KeyboardType.Text)
        }
    )
}

@Composable
fun LoginOrRegister(
    emailOnChange: (String) -> Unit,
    passwordOnChange: (String) -> Unit,
    nameOnChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TextsField(
            valueOnChanged = nameOnChange,
            label = "ingrese su nombre"
        )
        Spacer(modifier = Modifier.padding(10.dp))

        TextsField(
            valueOnChanged = emailOnChange,
            label = "ingrese su email",
            isEmail = true
        )
        Spacer(modifier = Modifier.padding(10.dp))

        TextsField(
            valueOnChanged = passwordOnChange,
            label = "ingrese su contraseña",
            isPassword = true
        )
    }
}

@Composable
fun AddClasses(
    email: String,
    password: String,
    name: String,
    viewModel: DataBaseViewModel,
    navController: NavController,
    onErrorMessage: (String) -> Unit // <-- nuevo callback
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Buttons("Login") {
            viewModel.singInWithEmailAndPassword(
                email = email,
                password = password,
                onSuccess = {
                    navController.navigate(AppScreen.ClassesHub.route)
                },
                onError = {
                    onErrorMessage(it) // muestra mensaje en snackbar
                }
            )
        }
        Spacer(modifier = Modifier.padding(5.dp))

        Buttons("Register") {
            viewModel.createUserWithEmailAndPassword(
                email = email,
                password = password,
                onSuccess = {
                    val uid = FirebaseAuth.getInstance().currentUser?.uid

                    if (uid != null) {

                        val profesorData = hashMapOf<String, Any>(
                            "uid" to uid,
                            "nombre" to name,
                            "email" to email,
                        )

                        FireBaseCRUD().addProfesor(profesorData)
                    }
                    navController.navigate(AppScreen.ClassesHub.route)
                },
                onError = {
                    onErrorMessage(it) // muestra mensaje en snackbar
                }
            )
        }
    }
}

@Composable
fun MainLoginOrRegister(
    viewModel: DataBaseViewModel,
    navController: NavController
) {
    var generalEmail by remember { mutableStateOf("") }
    var generalPassword by remember { mutableStateOf("") }
    var generalName by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }

    // Mostrar snackbar si hay error
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            errorMessage = null // Reset después de mostrarlo
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                topBarButton = false,
                topBarOnClick = { /*TODO*/ }
            )
        },
        containerColor = DarkBlue,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(15.dp))

            Text(
                text = "Ingreso como profesor",
                color = Color.White
            )

            LoginOrRegister(
                emailOnChange = { generalEmail = it },
                passwordOnChange = { generalPassword = it },
                nameOnChange = { generalName = it }
            )
            AddClasses(
                email = generalEmail,
                password = generalPassword,
                name = generalName,
                viewModel = viewModel,
                navController = navController,
                onErrorMessage = { errorMessage = it } // <-- nuevo
            )
        }
    }
}


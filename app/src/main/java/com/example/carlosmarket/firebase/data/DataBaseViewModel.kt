package com.example.carlosmarket.firebase.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class DataBaseViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val _longing = MutableLiveData(false)

    fun singInWithEmailAndPassword(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit // Cambiado: ahora recibe un mensaje
    ) = viewModelScope.launch {
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("TAG", "signInWithEmailAndPassword logueado")
                        onSuccess()
                    } else {
                        val errorMessage = when (val exception = task.exception) {
                            is FirebaseAuthInvalidUserException -> {
                                "No existe una cuenta con este correo"
                            }

                            is FirebaseAuthInvalidCredentialsException -> {
                                "Contraseña incorrecta o correo inválido"
                            }

                            else -> {
                                "Error al iniciar sesión: ${exception?.localizedMessage}"
                            }
                        }
                        Log.d("TAG", "signInWithEmailAndPassword error: $errorMessage")
                        onError(errorMessage)
                    }
                }
        } catch (e: Exception) {
            Log.e("TAG", "Exception: ${e.message}")
            onError("Ocurrió un error inesperado: ${e.message}")
        }
    }

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit  // Cambiamos: pasamos un mensaje de error
    ) {
        if (_longing.value == false) {
            _longing.value = true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("TAG", "createUserWithEmailAndPassword logueado")
                        onSuccess()
                    } else {
                        val errorMessage = when (val exception = task.exception) {
                            is FirebaseAuthUserCollisionException -> {
                                // Ya existe una cuenta con ese email
                                "Este correo ya está registrado"
                            }

                            is FirebaseAuthWeakPasswordException -> {
                                "Contraseña demasiado débil (mínimo 6 caracteres)"
                            }

                            is FirebaseAuthInvalidCredentialsException -> {
                                "Correo inválido"
                            }

                            else -> {
                                "Error desconocido al registrar: ${exception?.localizedMessage}"
                            }
                        }

                        Log.d("TAG", "createUserWithEmailAndPassword error: $errorMessage")
                        onError(errorMessage)
                    }
                    _longing.value = false
                }
        }
    }

}
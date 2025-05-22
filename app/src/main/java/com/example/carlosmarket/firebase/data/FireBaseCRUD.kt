package com.example.carlosmarket.firebase.data

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore

class FireBaseCRUD {
    private val db = Firebase.firestore

    // Agregar un profesor a Firestore
    fun addProfesor(professorData: HashMap<String, Any>){
        db.collection("profesor")
            .add(professorData)
            .addOnSuccessListener { documentReference ->
                Log.d("TAG", "Profesor agregado con ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error al agregar el profesor", e)
            }
    }

    // Agregar una clase con ID único
    fun addClass(classData: HashMap<String, Any?>) {
        val docRef = db.collection("clases").document() // Genera ID único
        val dataWithId = classData.toMutableMap().apply {
            put("id", docRef.id)//agrega el id a la data
        }

        docRef.set(dataWithId)//al documento recien creado le agrega la data
            .addOnSuccessListener {
                Log.d("TAG", "Clase agregada con ID: ${docRef.id}")
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error al agregar la clase", e)
            }
    }

    // Obtener todas las classes de un profesor
    fun getClasesDelProfesor(
        uid: String,
        onResult: (List<Clase>) -> Unit
    ): ListenerRegistration {
        return db.collection("clases")
            .whereEqualTo("profesorId", uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w("TAG", "Error escuchando clases", error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val clases = snapshot.documents.mapNotNull { doc ->
                        val nombre = doc.getString("nombre") ?: return@mapNotNull null
                        val profesorId = doc.getString("profesorId") ?: return@mapNotNull null
                        Clase(
                            id = doc.id,
                            nombre = nombre,
                            profesorId = profesorId,
                            items = listOf("pepe","papa")
                        )
                    }
                    onResult(clases)
                }
            }
    }

    //obtener los datos de una clase por ID
    fun getClaseById(
        claseId: String,
        onSuccess: (Clase) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("clases").document(claseId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val nombre = document.getString("nombre")
                    val profesorId = document.getString("profesorId")
                    val items = document.get("items") as? List<String> ?: emptyList()

                    if (nombre != null && profesorId != null) {
                        val clase = Clase(
                            id = document.id,
                            nombre = nombre,
                            profesorId = profesorId,
                            items = items
                        )
                        onSuccess(clase)
                    } else {
                        onFailure(Exception("Datos incompletos en la clase"))
                    }
                } else {
                    onFailure(Exception("Clase no encontrada"))
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    // Borrar clase por ID
    fun deleteClaseById(id: String) {
        db.collection("clases").document(id)
            .delete()
            .addOnSuccessListener {
                Log.d("TAG", "Clase eliminada con ID: $id")
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error al borrar la clase", e)
            }
    }
}

package com.example.myapplication.Presentation.Components.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun RecordScreen(navController: NavController) {
    var nombre by remember { mutableStateOf("") }
    var dni by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Registro",
            style = TextStyle(color = Color.Black, fontSize = 42.sp, fontWeight = FontWeight.Black)
        )

        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") }
        )

        TextField(
            value = dni,
            onValueChange = { dni = it },
            label = { Text("DNI") }
        )

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") }
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") }
        )

        Text(
            text = errorMessage,
            style = TextStyle(color = Color.Red)
        )

        Button(
            onClick = { signUpWithEmailAndPassword(navController, email, password, nombre, dni) { errorMessage = it } },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Registrarse")
        }
    }
}
private fun signUpWithEmailAndPassword(navController: NavController, email: String, password: String, nombre: String, dni: String, errorMessageSetter: (String) -> Unit) {
    val auth = FirebaseAuth.getInstance()
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                val db = FirebaseFirestore.getInstance()
                val userData = hashMapOf(
                    "nombre" to nombre,
                    "dni" to dni,
                    "email" to email
                )
                if (user != null) {
                    db.collection("users").document(user.uid)
                        .set(userData)
                        .addOnSuccessListener {
                            navController.navigate("StartScreen")
                        }
                        .addOnFailureListener { e ->
                            errorMessageSetter("Error al registrar en la base de datos: ${e.message}")
                        }
                }
            } else {
                errorMessageSetter("Error al registrar: ${task.exception?.message}")
            }
        }
}
package com.example.myapplication.Presentation.Components.Screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
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
fun LoginScreen(navController: NavController, userViewModel: UserViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "LOGIN",
            style = TextStyle(color = Color.Black, fontSize = 42.sp, fontWeight = FontWeight.Black)
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

        Row {
            Text(
                text = "¿No tienes cuenta?",
                color = Color.Black,
            )
            Text(
                text = "Regístrate aquí",
                color = Color.Blue,
                modifier = Modifier.clickable { navController.navigate("RecordScreen") }
            )
        }

        Button(
            onClick = { signInWithEmailAndPassword(navController, email, password, userViewModel) { errorMessage = it } },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Iniciar sesión")
        }

    }
}

private fun signInWithEmailAndPassword(navController: NavController, email: String, password: String, userViewModel: UserViewModel, errorMessageSetter: (String) -> Unit) {
    val auth = FirebaseAuth.getInstance()
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                user?.let {
                    val db = FirebaseFirestore.getInstance()
                    db.collection("users").document(user.uid).get()
                        .addOnSuccessListener { document ->
                            if (document != null && document.exists()) {
                                val userData = document.toObject(User::class.java)
                                userData?.let {
                                    userViewModel.setUser(it)
                                    navController.navigate("StartScreen")
                                }
                            } else {
                                errorMessageSetter("No such document")
                            }
                        }
                        .addOnFailureListener { exception ->
                            errorMessageSetter("get failed with $exception")
                        }
                }
            } else {
                errorMessageSetter("Contraseña o correo incorrecto")
            }
        }
}




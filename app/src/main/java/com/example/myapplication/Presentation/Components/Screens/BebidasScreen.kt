package com.example.myapplication.Presentation.Components.Screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.Model.Plato
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BebidasScreen(navController: NavController, userViewModel: UserViewModel) {
    var platosBebidas by remember { mutableStateOf<List<Plato>>(emptyList()) }
    val selectedPlatos = remember { mutableStateListOf<Plato>() }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val querySnapshot = FirebaseFirestore.getInstance()
            .collection("bebidas")
            .get()
            .await()

        val platosList = mutableListOf<Plato>()
        for (document in querySnapshot.documents) {
            val nombre = document.getString("nombre") ?: ""
            val precio = document.getDouble("precio") ?: 0.0
            val plato = Plato(nombre, precio)
            platosList.add(plato)
        }
        platosBebidas = platosList
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Confirmar Pedido") },
            text = { Text(text = "¿Estás seguro de que deseas confirmar tu pedido?") },
            confirmButton = {
                Button(
                    onClick = {
                        val user = userViewModel.user.value
                        if (user != null) {
                            selectedPlatos.forEach { plato ->
                                val db = FirebaseFirestore.getInstance()
                                val compraData = hashMapOf(
                                    "clienteNombre" to user.nombre,
                                    "productoNombre" to plato.nombre,
                                    "precio" to plato.precio,
                                    "cantidad" to 1,
                                    "compraCompletada" to false
                                )
                                db.collection("compras")
                                    .add(compraData)
                                    .addOnSuccessListener {
                                        println("Compra agregada exitosamente")
                                    }
                                    .addOnFailureListener { e ->
                                        println("Error al agregar la compra: $e")
                                    }
                            }
                        }
                        showDialog = false
                        navController.navigate("startScreen") // Navega automáticamente a la pantalla de inicio
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Bebidas",
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(platosBebidas) { plato ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp, horizontal = 16.dp)
                ) {
                    Text("${plato.nombre}, Precio: ${plato.precio}")
                    Spacer(modifier = Modifier.weight(1f))
                    Checkbox(
                        checked = plato in selectedPlatos,
                        onCheckedChange = {
                            if (it) {
                                selectedPlatos.add(plato)
                            } else {
                                selectedPlatos.remove(plato)
                            }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                showDialog = true // Mostrar el diálogo de confirmación
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Enviar al carrito")
        }
    }
}

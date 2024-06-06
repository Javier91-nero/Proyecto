package com.example.myapplication.Presentation.Components.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.Model.Compra
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Composable
fun ShoppingScreen(userViewModel: UserViewModel) {
    val clienteNombre = userViewModel.user.value?.nombre ?: ""

    var compras by remember { mutableStateOf<List<Compra>>(emptyList()) }

    LaunchedEffect(clienteNombre) {
        val querySnapshot = FirebaseFirestore.getInstance()
            .collection("compras")
            .whereEqualTo("clienteNombre", clienteNombre)
            .whereEqualTo("compraCompletada", false)
            .get()
            .await()

        val comprasList = mutableListOf<Compra>()
        for (document in querySnapshot.documents) {
            val id = document.id
            val productoNombre = document.getString("productoNombre") ?: ""
            val precio = document.getDouble("precio") ?: 0.0
            val cantidad = document.getLong("cantidad")?.toInt() ?: 0
            val compra = Compra(id, productoNombre, precio, cantidad)
            comprasList.add(compra)
        }
        compras = comprasList
    }

    val total = compras.sumByDouble { it.precio * it.cantidad }

    fun actualizarCantidadEnFirestore(compraId: String, nuevaCantidad: Int) {
        FirebaseFirestore.getInstance()
            .collection("compras")
            .document(compraId)
            .update("cantidad", nuevaCantidad)
            .addOnSuccessListener {
                compras = compras.map {
                    if (it.id == compraId) it.copy(cantidad = nuevaCantidad) else it
                }
            }
    }

    fun aumentarCantidad(compra: Compra) {
        if (compra.cantidad < 10) {
            val nuevaCantidad = compra.cantidad + 1
            actualizarCantidadEnFirestore(compra.id, nuevaCantidad)
        }
    }

    fun disminuirCantidad(compra: Compra) {
        if (compra.cantidad > 1) {
            val nuevaCantidad = compra.cantidad - 1
            actualizarCantidadEnFirestore(compra.id, nuevaCantidad)
        }
    }

    fun eliminarPedido(compraId: String) {
        FirebaseFirestore.getInstance()
            .collection("compras")
            .document(compraId)
            .delete()
            .addOnSuccessListener {
                compras = compras.filterNot { it.id == compraId }
            }
    }

    fun confirmarCompra() {
        FirebaseFirestore.getInstance()
            .collection("compras")
            .whereEqualTo("clienteNombre", clienteNombre)
            .whereEqualTo("compraCompletada", false)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    document.reference.update("compraCompletada", true)
                }
                compras = emptyList()
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Compras",
            style = TextStyle(color = Color.Black, fontSize = 42.sp, fontWeight = FontWeight.Black),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(compras) { compra ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp, horizontal = 16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "${compra.productoNombre}, Precio: ${compra.precio}, Cantidad: ${compra.cantidad}"
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp)
                    ) {
                        // Botón de decremento
                        Button(
                            onClick = { disminuirCantidad(compra) },
                            enabled = compra.cantidad > 1
                        ) {
                            Text("-")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Mostrar la cantidad
                        Text(
                            text = compra.cantidad.toString(),
                            style = TextStyle(fontSize = 18.sp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // Botón de incremento
                        Button(
                            onClick = { aumentarCantidad(compra) },
                            enabled = compra.cantidad < 10
                        ) {
                            Text("+")
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // Botón para eliminar el pedido
                        IconButton(onClick = { eliminarPedido(compra.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                        }
                    }
                }
            }

            // Mostrar el precio total y botón de confirmar compra
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp, horizontal = 16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Total: $total",
                        style = TextStyle(color = Color.Black, fontSize = 24.sp),
                        modifier = Modifier.padding(vertical = 16.dp)
                    )

                    Button(onClick = { confirmarCompra() }) {
                        Text(text = "Confirmar compra")
                    }

                    Spacer(modifier = Modifier.height(40.dp)) // Espacio adicional después del botón
                }
            }
        }
    }
}
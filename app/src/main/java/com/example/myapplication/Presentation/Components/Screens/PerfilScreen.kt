package com.example.myapplication.Presentation.Components.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PerfilScreen(userViewModel: UserViewModel) {
    val user = userViewModel.user.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Perfil",
            style = TextStyle(
                color = Color.Black,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (user != null) {
            ProfileInfoRow(label = "Nombre", value = user.nombre)
            ProfileInfoRow(label = "DNI", value = user.dni)
            ProfileInfoRow(label = "Correo electrónico", value = user.email)
        } else {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            Text(text = "Cargando datos del usuario...")
        }
    }
}

@Composable
fun ProfileInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label:",
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium)
        )
        Text(
            text = value,
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Normal)
        )
    }
}
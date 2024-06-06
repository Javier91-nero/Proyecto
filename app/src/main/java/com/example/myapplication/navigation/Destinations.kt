package com.example.myapplication.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Destinations(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object StartScreen: Destinations("StartScreen", "Inicio", Icons.Filled.Home)
    object ShoppingScreen: Destinations("ShoppingScreen", "Compras", Icons.Filled.ShoppingCart)
    object Screen3: Destinations("Screen3", "Screen 3", Icons.Filled.LocationOn)
    object PerfilScreen: Destinations("PerfilScreen", "Perfil", Icons.Filled.Person)
    object LoginScreen: Destinations("LoginScreen", "Cerrar Sesiòn", Icons.Filled.ExitToApp)
}
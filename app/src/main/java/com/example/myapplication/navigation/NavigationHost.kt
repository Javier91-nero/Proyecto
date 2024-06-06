package com.example.myapplication.navigation


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication.Presentation.Components.Screens.BebidasScreen
import com.example.myapplication.Presentation.Components.Screens.StartScreen
import com.example.myapplication.Presentation.Components.Screens.ShoppingScreen
import com.example.myapplication.Presentation.Components.Screens.Screen3
import com.example.myapplication.Presentation.Components.Screens.LoginScreen
import com.example.myapplication.Presentation.Components.Screens.PerfilScreen
import com.example.myapplication.Presentation.Components.Screens.PlatosEntradaScreen
import com.example.myapplication.Presentation.Components.Screens.PlatosFondoScreen
import com.example.myapplication.Presentation.Components.Screens.RecordScreen
import com.example.myapplication.Presentation.Components.Screens.UserViewModel

import com.example.myapplication.navigation.Destinations.StartScreen
import com.example.myapplication.navigation.Destinations.ShoppingScreen
import com.example.myapplication.navigation.Destinations.Screen3
import com.example.myapplication.navigation.Destinations.LoginScreen
import com.example.myapplication.navigation.Destinations.PerfilScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    userViewModel: UserViewModel
) {
    NavHost(navController = navController, startDestination = Destinations.LoginScreen.route) {
        composable(Destinations.StartScreen.route) {
            StartScreen(navController = navController)
        }
        composable(Destinations.ShoppingScreen.route) {
            ShoppingScreen(userViewModel)
        }
        composable(Destinations.Screen3.route) {
            Screen3()
        }
        composable(Destinations.PerfilScreen.route) {
            PerfilScreen(userViewModel)
        }
        composable(Destinations.LoginScreen.route) {
            LoginScreen(navController, userViewModel)
        }
        composable("RecordScreen") {
            RecordScreen(navController)
        }
        // Menu
        composable("PlatosFondoScreen") {
            PlatosFondoScreen(navController, userViewModel)
        }
        composable("BebidasScreen") {
            BebidasScreen(navController, userViewModel)
        }
        composable("PlatosEntradaScreen") {
            PlatosEntradaScreen(navController, userViewModel)
        }
    }
}


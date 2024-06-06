package com.example.myapplication

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DrawerValue
import androidx.compose.material.FabPosition
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.Presentation.Components.Components.BottomNavigationBar
import com.example.myapplication.Presentation.Components.Components.Drawer
import com.example.myapplication.Presentation.Components.Components.TopBar
import com.example.myapplication.Presentation.Components.Screens.UserViewModel
import com.example.myapplication.navigation.Destinations
import com.example.myapplication.navigation.NavigationHost
import com.example.myapplication.ui.theme.MyApplicationTheme


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState(
        drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    )
    val scope = rememberCoroutineScope()
    val userViewModel: UserViewModel = viewModel()

    // Lista de items de navegación para los screens donde queremos mostrar BottomNavigationBar
    val navigationItems = listOf(
        Destinations.StartScreen,
        Destinations.ShoppingScreen,
        Destinations.Screen3,
        Destinations.PerfilScreen,
        Destinations.LoginScreen
    )

    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        scaffoldState = scaffoldState,
        bottomBar = {
            if (currentDestination in listOf("StartScreen", "ShoppingScreen", "Screen3", "PerfilScreen")) {
                BottomNavigationBar(navController = navController, items = navigationItems)
            }
        },
        isFloatingActionButtonDocked = false,
        floatingActionButtonPosition = FabPosition.End,
        topBar = {
            if (currentDestination in listOf("StartScreen", "ShoppingScreen", "Screen3", "PerfilScreen")) {
                TopBar(scope = scope, scaffoldState = scaffoldState, currentScreen = currentDestination ?: "")
            }
        },
        drawerContent = {
            Drawer(scope, scaffoldState, navController, items = navigationItems, userViewModel = userViewModel)
        }
    ) { innerPadding ->
        NavigationHost(Modifier.padding(innerPadding), navController, userViewModel)
    }
}


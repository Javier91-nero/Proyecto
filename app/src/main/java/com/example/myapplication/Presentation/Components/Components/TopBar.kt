package com.example.myapplication.Presentation.Components.Components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TopBar(
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    currentScreen: String
) {
    if (currentScreen in listOf("StartScreen", "ShoppingScreen", "Screen3", "PerfilScreen")) {
        TopAppBar(
            title = { Text("Cuy") },
            backgroundColor = Color.Blue,
            contentColor = Color.White,
            navigationIcon = {
                IconButton(onClick = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                }) {
                    Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu Icon")
                }
            },
            actions = {
                IconButton(onClick = {
                    // TODO: Display SnackBar
                }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
                }
            }
        )
    }
}

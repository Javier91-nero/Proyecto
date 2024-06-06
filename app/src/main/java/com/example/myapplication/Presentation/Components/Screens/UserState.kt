package com.example.myapplication.Presentation.Components.Screens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

data class User(
    val nombre: String = "",
    val dni: String = "",
    val email: String = ""
)

class UserViewModel : ViewModel() {
    var user: MutableState<User?> = mutableStateOf(null)
        private set

    fun setUser(user: User) {
        this.user.value = user
    }
}
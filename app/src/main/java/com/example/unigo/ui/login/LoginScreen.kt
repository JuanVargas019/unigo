package com.example.unigo.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.unigo.navigation.Screen

@Composable
fun LoginScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Pantalla de Login - en construcción")

        // Botón temporal de prueba para verificar la navegación.
        Button(onClick = { navController.navigate(Screen.Home.route) }) {
            Text(text = "Entrar (temp)")
        }
    }
}

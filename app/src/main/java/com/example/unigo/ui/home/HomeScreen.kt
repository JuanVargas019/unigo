package com.example.unigo.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.unigo.navigation.Screen

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Pantalla de Home - en construcción")

        // Botones temporales de prueba para verificar la navegación.
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate(Screen.Horario.route) }) {
            Text(text = "Ir a Horario (temp)")
        }
        Button(onClick = { navController.navigate(Screen.Tareas.route) }) {
            Text(text = "Ir a Tareas (temp)")
        }
        Button(onClick = { navController.navigate(Screen.Calendario.route) }) {
            Text(text = "Ir a Calendario (temp)")
        }
        Button(onClick = { navController.navigate(Screen.Calificaciones.route) }) {
            Text(text = "Ir a Calificaciones (temp)")
        }
        Button(onClick = { navController.navigate(Screen.Perfil.route) }) {
            Text(text = "Ir a Perfil (temp)")
        }
        Button(onClick = { navController.navigate(Screen.Login.route) }) {
            Text(text = "Cerrar sesión (temp)")
        }
    }
}

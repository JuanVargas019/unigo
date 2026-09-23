package com.example.unigo.ui.tareas

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
fun TareasScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Pantalla de Tareas - en construcción")

        // Botón temporal de prueba para verificar la navegación.
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate(Screen.Home.route) }) {
            Text(text = "Volver al Home (temp)")
        }
    }
}

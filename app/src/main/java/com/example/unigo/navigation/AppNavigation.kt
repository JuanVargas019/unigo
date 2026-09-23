package com.example.unigo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.unigo.ui.calendario.CalendarioScreen
import com.example.unigo.ui.calificaciones.CalificacionesScreen
import com.example.unigo.ui.home.HomeScreen
import com.example.unigo.ui.horario.HorarioScreen
import com.example.unigo.ui.login.LoginScreen
import com.example.unigo.ui.perfil.PerfilScreen
import com.example.unigo.ui.tareas.TareasScreen

/**
 * NavHost central de la app.
 * Aquí se registran todas las pantallas. Cada integrante solo
 * completa el contenido de SU pantalla dentro de ui/.
 */
@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.Horario.route) {
            HorarioScreen(navController = navController)
        }
        composable(Screen.Tareas.route) {
            TareasScreen(navController = navController)
        }
        composable(Screen.Calendario.route) {
            CalendarioScreen(navController = navController)
        }
        composable(Screen.Calificaciones.route) {
            CalificacionesScreen(navController = navController)
        }
        composable(Screen.Perfil.route) {
            PerfilScreen(navController = navController)
        }
    }
}

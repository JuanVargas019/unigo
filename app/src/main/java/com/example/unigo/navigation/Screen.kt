package com.example.unigo.navigation

/**
 * Define las rutas de navegación de toda la app.
 * Cada pantalla de UniGo tiene su propio objeto con su route (String).
 */
sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Home : Screen("home")
    data object Horario : Screen("horario")
    data object Tareas : Screen("tareas")
    data object Calendario : Screen("calendario")
    data object Calificaciones : Screen("calificaciones")
    data object Perfil : Screen("perfil")
}

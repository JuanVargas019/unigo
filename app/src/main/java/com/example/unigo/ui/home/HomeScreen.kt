package com.example.unigo.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.unigo.navigation.Screen
import com.example.unigo.ui.horario.HorarioEnMemoria
import com.example.unigo.ui.horario.diaActualEnEspanol

/**
 * Acceso visual desde el Home hacia otra pantalla de la app.
 * Solo navega usando la ruta ya definida en [Screen].
 */
private data class AccesoHome(
    val titulo: String,
    val descripcion: String,
    val emoji: String,
    val ruta: String
)

private val accesosHome: List<AccesoHome> = listOf(
    AccesoHome(
        titulo = "Horario",
        descripcion = "Tus clases de la semana",
        emoji = "🕐",
        ruta = Screen.Horario.route
    ),
    AccesoHome(
        titulo = "Tareas",
        descripcion = "Pendientes y entregas",
        emoji = "✅",
        ruta = Screen.Tareas.route
    ),
    AccesoHome(
        titulo = "Calendario",
        descripcion = "Fechas importantes",
        emoji = "📅",
        ruta = Screen.Calendario.route
    ),
    AccesoHome(
        titulo = "Calificaciones",
        descripcion = "Tus notas por materia",
        emoji = "⭐",
        ruta = Screen.Calificaciones.route
    ),
    AccesoHome(
        titulo = "Perfil",
        descripcion = "Tu información personal",
        emoji = "👤",
        ruta = Screen.Perfil.route
    )
)

/**
 * Pantalla principal del estudiante.
 * Muestra el nombre de la app, un saludo, el resumen del día
 * y accesos de navegación al resto de módulos.
 */
@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 480.dp)
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Nombre de la aplicación
            Text(
                text = "UniGo",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            // Saludo al estudiante
            Text(
                text = "Hola, estudiante 👋",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Este es tu día en un vistazo.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Resumen sencillo del día
            TarjetaResumenDelDia()

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Menú principal",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Accesos al resto de módulos (dos columnas)
            accesosHome.chunked(2).forEach { fila ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    fila.forEach { acceso ->
                        TarjetaAcceso(
                            acceso = acceso,
                            onClick = { navController.navigate(acceso.ruta) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (fila.size == 1) {
                        // Rellena el hueco cuando la fila queda incompleta.
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

/** Tarjeta con el resumen del día: cantidad de clases y la primera del día. */
@Composable
private fun TarjetaResumenDelDia() {
    val diaHoy = diaActualEnEspanol()
    val clasesDeHoy = HorarioEnMemoria.clasesDelDia(diaHoy)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Resumen del día",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (clasesDeHoy.isEmpty()) {
                Text(
                    text = "Hoy es ${diaHoy.lowercase()} y no tienes clases programadas. 🎉",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            } else {
                Text(
                    text = "Hoy es ${diaHoy.lowercase()} · ${clasesDeHoy.size} clases",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                val primeraClase = clasesDeHoy.first()
                Text(
                    text = "Primera clase: ${primeraClase.horaInicio} · ${primeraClase.materia}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

/** Tarjeta de acceso a un módulo de la app. */
@Composable
private fun TarjetaAcceso(
    acceso: AccesoHome,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = acceso.emoji,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = acceso.titulo,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = acceso.descripcion,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

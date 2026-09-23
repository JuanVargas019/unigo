package com.example.unigo.ui.calendario

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.unigo.navigation.Screen
import com.example.unigo.ui.tareas.Tarea
import com.example.unigo.ui.tareas.TareasEnMemoria
import com.example.unigo.ui.tareas.desgloseFecha
import com.example.unigo.ui.tareas.fechaEnMilisegundos
import com.example.unigo.ui.tareas.mesesEnEspanol

/**
 * Pantalla de Calendario del módulo académico.
 *
 * Muestra las fechas de entrega de las tareas agrupadas por día,
 * de la fecha más cercana a la más lejana. Comparte la lista de
 * tareas con TareasScreen a través de [TareasEnMemoria].
 */
@Composable
fun CalendarioScreen(navController: NavController) {
    val tareas = TareasEnMemoria.tareas

    // Agrupa las tareas por fecha y ordena los días de menor a mayor.
    val tareasPorFecha: List<Pair<String, List<Tarea>>> = tareas
        .groupBy { it.fechaEntrega }
        .toList()
        .sortedBy { (fecha, _) -> fechaEnMilisegundos(fecha) }

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
            // Volver a la pantalla anterior (Home)
            TextButton(onClick = { navController.navigateUp() }) {
                Text(text = "← Volver")
            }

            Text(
                text = "Calendario",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Fechas de entrega de tus tareas",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Navegación al módulo de Tareas (misma ruta ya definida)
            OutlinedButton(
                onClick = { navController.navigate(Screen.Tareas.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Ver tareas")
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (tareasPorFecha.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "No hay fechas registradas. 📅",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Agrega tareas con fecha de entrega desde la pantalla de Tareas.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                tareasPorFecha.forEach { (fecha, tareasDeLaFecha) ->
                    TarjetaFecha(
                        fecha = fecha,
                        tareas = tareasDeLaFecha
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            Text(
                text = "Los datos se actualizan según las tareas cargadas en la app.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/** Tarjeta de un día del calendario con todas sus entregas. */
@Composable
private fun TarjetaFecha(fecha: String, tareas: List<Tarea>) {
    val desglose = desgloseFecha(fecha)
    val dia = desglose?.first
    val mes = desglose?.second
    val anio = desglose?.third

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Bloque de fecha: día, mes y año
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = dia?.toString()?.padStart(2, '0') ?: "--",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = mes?.let { mesesEnEspanol[it] } ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = anio?.toString() ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Entregas del día
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (tareas.size == 1) "1 entrega" else "${tareas.size} entregas",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                tareas.forEachIndexed { index, tarea ->
                    if (index > 0) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    FilaTareaCalendario(tarea = tarea)
                }
            }
        }
    }
}

/** Fila con una tarea dentro del calendario: nombre, materia y estado. */
@Composable
private fun FilaTareaCalendario(tarea: Tarea) {
    val completada = tarea.completada

    Column {
        Text(
            text = if (completada) "✓ ${tarea.nombre}" else "• ${tarea.nombre}",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = if (completada) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )
        Text(
            text = tarea.materia,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = if (completada) "Completada" else "Pendiente",
            style = MaterialTheme.typography.labelSmall,
            color = if (completada) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.tertiary
            }
        )
    }
}

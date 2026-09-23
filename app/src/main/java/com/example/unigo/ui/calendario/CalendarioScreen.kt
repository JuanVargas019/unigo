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
import com.example.unigo.ui.horario.ClaseHorario
import com.example.unigo.ui.horario.HorarioEnMemoria
import com.example.unigo.ui.horario.proximaFechaDeClase
import com.example.unigo.ui.tareas.Tarea
import com.example.unigo.ui.tareas.TareasEnMemoria
import com.example.unigo.ui.tareas.desgloseFecha
import com.example.unigo.ui.tareas.fechaEnMilisegundos
import com.example.unigo.ui.tareas.mesesEnEspanol

/**
 * Pantalla de Calendario del módulo académico.
 *
 * Muestra las fechas de entrega de las tareas y las clases del horario
 * agrupadas por día, de la fecha más cercana a la más lejana.
 * Comparte los datos con TareasScreen a través de [TareasEnMemoria]
 * y con HorarioScreen a través de [HorarioEnMemoria] (solo lectura:
 * las clases se crean únicamente desde el Horario).
 */
@Composable
fun CalendarioScreen(navController: NavController) {
    val tareas = TareasEnMemoria.tareas
    val clases = HorarioEnMemoria.clases

    // Agrupa las tareas por fecha de entrega.
    val tareasPorFecha: Map<String, List<Tarea>> = tareas.groupBy { it.fechaEntrega }

    // Agrupa las clases por su próximo día de la semana (dd/MM/yyyy).
    val clasesPorFecha: Map<String, List<ClaseHorario>> = clases
        .groupBy { proximaFechaDeClase(it.dia).orEmpty() }
        .filterKeys { it.isNotEmpty() }

    // Une las fechas de tareas y de clases, ordenadas de menor a mayor.
    val fechas: List<String> = (tareasPorFecha.keys + clasesPorFecha.keys)
        .distinct()
        .sortedBy { fecha -> fechaEnMilisegundos(fecha) }

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
                text = "Fechas de entrega de tus tareas y tus próximas clases",
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

            if (fechas.isEmpty()) {
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
                            text = "Agrega tareas desde la pantalla de Tareas o clases desde el Horario.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                fechas.forEach { fecha ->
                    TarjetaFecha(
                        fecha = fecha,
                        tareas = tareasPorFecha[fecha].orEmpty(),
                        clases = clasesPorFecha[fecha].orEmpty()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            Text(
                text = "Los datos se actualizan según las tareas y clases cargadas en la app.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/** Tarjeta de un día del calendario con todas sus entregas y clases. */
@Composable
private fun TarjetaFecha(
    fecha: String,
    tareas: List<Tarea>,
    clases: List<ClaseHorario>
) {
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

            // Entregas y clases del día
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = resumenDelDia(tareas.size, clases.size),
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

                if (tareas.isNotEmpty() && clases.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                clases.forEachIndexed { index, clase ->
                    if (index > 0) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    FilaClaseCalendario(clase = clase)
                }
            }
        }
    }
}

/** Texto del encabezado de una fecha: entregas, clases o ambas. */
private fun resumenDelDia(cantidadTareas: Int, cantidadClases: Int): String {
    val partes = buildList {
        if (cantidadTareas > 0) {
            add(if (cantidadTareas == 1) "1 entrega" else "$cantidadTareas entregas")
        }
        if (cantidadClases > 0) {
            add(if (cantidadClases == 1) "1 clase" else "$cantidadClases clases")
        }
    }
    return partes.joinToString(" · ")
}

/** Fila con una clase del horario dentro del calendario: materia, día y horario. */
@Composable
private fun FilaClaseCalendario(clase: ClaseHorario) {
    Column {
        Text(
            text = "• ${clase.materia}",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "${clase.dia} · ${clase.horaInicio} - ${clase.horaFin}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "Clase",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.tertiary
        )
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

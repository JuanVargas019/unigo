package com.example.unigo.ui.tareas

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.unigo.navigation.Screen

/**
 * Pantalla de Tareas del módulo académico.
 *
 * Muestra la lista de tareas (en memoria), permite agregar tareas nuevas
 * con su fecha de entrega, marcarlas como completadas y eliminarlas
 * con el ícono de basura de cada tarjeta.
 * Desde aquí también se puede navegar al Calendario.
 */
@Composable
fun TareasScreen(navController: NavController) {
    val tareas = TareasEnMemoria.tareas
    var mostrarFormulario by remember { mutableStateOf(false) }
    var tareaAEliminar by remember { mutableStateOf<Tarea?>(null) }

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
                text = "Tareas",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Organiza tus entregas del semestre",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Acciones principales: agregar tarea y ver el calendario
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { mostrarFormulario = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "+ Nueva tarea")
                }
                OutlinedButton(
                    onClick = { navController.navigate(Screen.Calendario.route) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Ver calendario")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Lista de tareas
            if (tareas.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Todavía no hay tareas. 📝",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Usa el botón «+ Nueva tarea» para agregar la primera.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                tareas.forEach { tarea ->
                    TarjetaTarea(
                        tarea = tarea,
                        onAlternarEstado = {
                            TareasEnMemoria.alternarCompletada(tarea.id)
                        },
                        onEliminar = { tareaAEliminar = tarea }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Text(
                text = "Las tareas se guardan en memoria mientras la aplicación está abierta.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Formulario para agregar una tarea nueva
    if (mostrarFormulario) {
        DialogoNuevaTarea(
            onCerrar = { mostrarFormulario = false },
            onGuardar = { nombre, materia, fecha ->
                TareasEnMemoria.agregarTarea(
                    nombre = nombre,
                    materia = materia,
                    fechaEntrega = fecha
                )
                mostrarFormulario = false
            }
        )
    }

    // Confirmación para eliminar una tarea (mismo patrón que en Horario)
    tareaAEliminar?.let { tarea ->
        DialogoConfirmarEliminarTarea(
            tarea = tarea,
            onConfirmar = {
                TareasEnMemoria.eliminarTarea(tarea.id)
                tareaAEliminar = null
            },
            onCancelar = { tareaAEliminar = null }
        )
    }
}

/** Tarjeta con la información de una tarea, su checkbox y el ícono de eliminar. */
@Composable
private fun TarjetaTarea(
    tarea: Tarea,
    onAlternarEstado: () -> Unit,
    onEliminar: () -> Unit
) {
    val completada = tarea.completada

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (completada) {
                MaterialTheme.colorScheme.secondaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tarea.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (completada) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                    textDecoration = if (completada) TextDecoration.LineThrough else null
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Materia: ${tarea.materia}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Fecha de entrega: ${tarea.fechaEntrega}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (completada) "✓ Completada" else "● Pendiente",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (completada) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.tertiary
                    }
                )
            }

            Checkbox(
                checked = completada,
                onCheckedChange = { onAlternarEstado() }
            )

            // Eliminar la tarea (y del calendario)
            IconButton(onClick = onEliminar) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar ${tarea.nombre}",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

/** Dialogo sencillo para confirmar la eliminación de una tarea. */
@Composable
private fun DialogoConfirmarEliminarTarea(
    tarea: Tarea,
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancelar,
        title = {
            Text(
                text = "¿Eliminar esta tarea?",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(text = "${tarea.nombre} · ${tarea.materia} · ${tarea.fechaEntrega}")
        },
        confirmButton = {
            Button(
                onClick = onConfirmar,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(text = "Sí, eliminar")
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) {
                Text(text = "Cancelar")
            }
        }
    )
}

/** Dialogo con el formulario para crear una tarea nueva. */
@Composable
private fun DialogoNuevaTarea(
    onCerrar: () -> Unit,
    onGuardar: (nombre: String, materia: String, fecha: String) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var materia by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onCerrar,
        title = {
            Text(
                text = "Nueva tarea",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text(text = "Nombre de la tarea") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = materia,
                    onValueChange = { materia = it },
                    label = { Text(text = "Materia") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = fecha,
                    onValueChange = { fecha = it },
                    label = { Text(text = "Fecha de entrega (dd/mm/aaaa)") },
                    placeholder = { Text(text = "25/09/2026") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                if (error.isNotEmpty()) {
                    Text(
                        text = error,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val fechaLimpia = fecha.trim()
                    error = when {
                        nombre.isBlank() -> "Escribe el nombre de la tarea."
                        materia.isBlank() -> "Escribe la materia."
                        !fechaValida(fechaLimpia) -> "La fecha debe ser válida con formato dd/mm/aaaa."
                        else -> {
                            onGuardar(nombre.trim(), materia.trim(), fechaLimpia)
                            ""
                        }
                    }
                }
            ) {
                Text(text = "Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onCerrar) {
                Text(text = "Cancelar")
            }
        }
    )
}

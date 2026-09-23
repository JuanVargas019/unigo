package com.example.unigo.ui.horario

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

/**
 * Pantalla de horario del estudiante.
 *
 * Muestra las clases guardadas en [HorarioEnMemoria] organizadas por día
 * en tarjetas fáciles de leer, permite agregar clases nuevas con un
 * formulario simple (materia, día y horario) y eliminar cualquier clase
 * con el ícono de basura de cada fila.
 */
@Composable
fun HorarioScreen(navController: NavController) {
    var mostrarFormulario by remember { mutableStateOf(false) }
    var claseAEliminar by remember { mutableStateOf<ClaseHorario?>(null) }
    val horario = HorarioEnMemoria.porDia()

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
                text = "Horario",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Horario académico del semestre",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Agregar una clase nueva al horario
            Button(
                onClick = { mostrarFormulario = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "+ Nueva clase")
            }

            Spacer(modifier = Modifier.height(24.dp))

            horario.forEach { dia ->
                TarjetaDia(
                    dia = dia,
                    onEliminar = { clase -> claseAEliminar = clase }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text(
                text = "Las clases se guardan en memoria mientras la aplicación está abierta.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Formulario para agregar una clase nueva
    if (mostrarFormulario) {
        DialogoNuevaClase(
            onCerrar = { mostrarFormulario = false },
            onGuardar = { materia, dia, horaInicio, horaFin ->
                HorarioEnMemoria.agregarClase(
                    materia = materia,
                    dia = dia,
                    horaInicio = horaInicio,
                    horaFin = horaFin
                )
                mostrarFormulario = false
            }
        )
    }

    // Confirmación para eliminar una clase (ejemplo o agregada por el usuario)
    claseAEliminar?.let { clase ->
        DialogoConfirmarEliminar(
            clase = clase,
            onConfirmar = {
                HorarioEnMemoria.eliminarClase(clase.id)
                claseAEliminar = null
            },
            onCancelar = { claseAEliminar = null }
        )
    }
}

/** Tarjeta con todas las clases de un día. */
@Composable
private fun TarjetaDia(
    dia: DiaHorario,
    onEliminar: (ClaseHorario) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = dia.dia,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (dia.clases.isEmpty()) {
                Text(
                    text = "Sin clases este día",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                dia.clases.forEachIndexed { index, clase ->
                    if (index > 0) {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    FilaClase(
                        clase = clase,
                        onEliminar = { onEliminar(clase) }
                    )
                }
            }
        }
    }
}

/** Fila con el horario de una clase: horas a la izquierda, materia y basura a la derecha. */
@Composable
private fun FilaClase(
    clase: ClaseHorario,
    onEliminar: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Bloque de horario
        Column(modifier = Modifier.width(96.dp)) {
            Text(
                text = clase.horaInicio,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = clase.horaFin,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Materia
        Text(
            text = clase.materia,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )

        // Eliminar la clase del horario (y del calendario)
        IconButton(
            onClick = onEliminar,
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Eliminar ${clase.materia}",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

/** Dialogo sencillo para confirmar la eliminación de una clase. */
@Composable
private fun DialogoConfirmarEliminar(
    clase: ClaseHorario,
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancelar,
        title = {
            Text(
                text = "¿Eliminar esta clase?",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(text = "${clase.materia} · ${clase.dia} · ${clase.horaInicio} - ${clase.horaFin}")
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

/** Dialogo con el formulario para crear una clase nueva. */
@Composable
private fun DialogoNuevaClase(
    onCerrar: () -> Unit,
    onGuardar: (materia: String, dia: String, horaInicio: String, horaFin: String) -> Unit
) {
    var materia by remember { mutableStateOf("") }
    var dia by remember { mutableStateOf(diasConClases.first()) }
    var horaInicio by remember { mutableStateOf("") }
    var horaFin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onCerrar,
        title = {
            Text(
                text = "Nueva clase",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = materia,
                    onValueChange = { materia = it },
                    label = { Text(text = "Nombre de la materia") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Selector simple de día (Lunes a Viernes)
                Text(
                    text = "Día",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    diasConClases.forEach { diaOpcion ->
                        val seleccionado = dia.equals(diaOpcion, ignoreCase = true)
                        OutlinedButton(
                            onClick = { dia = diaOpcion },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (seleccionado) {
                                    MaterialTheme.colorScheme.primaryContainer
                                } else {
                                    MaterialTheme.colorScheme.surface
                                },
                                contentColor = if (seleccionado) {
                                    MaterialTheme.colorScheme.onPrimaryContainer
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            )
                        ) {
                            Text(text = diaOpcion.take(3))
                        }
                    }
                }

                OutlinedTextField(
                    value = horaInicio,
                    onValueChange = { horaInicio = it },
                    label = { Text(text = "Hora de inicio") },
                    placeholder = { Text(text = "7:00 AM") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = horaFin,
                    onValueChange = { horaFin = it },
                    label = { Text(text = "Hora de fin") },
                    placeholder = { Text(text = "9:00 AM") },
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
                    error = when {
                        materia.isBlank() -> "Escribe el nombre de la materia."
                        horaInicio.isBlank() -> "Escribe la hora de inicio."
                        horaFin.isBlank() -> "Escribe la hora de fin."
                        horaInicio.trim().equals(horaFin.trim(), ignoreCase = true) ->
                            "La hora de fin debe ser distinta a la de inicio."
                        else -> {
                            onGuardar(
                                materia.trim(),
                                dia,
                                horaInicio.trim(),
                                horaFin.trim()
                            )
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

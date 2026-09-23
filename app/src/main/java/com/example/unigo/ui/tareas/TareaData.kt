package com.example.unigo.ui.tareas

import androidx.compose.runtime.mutableStateListOf
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Modelo simple de una tarea académica.
 *
 * La fecha de entrega se guarda como texto con formato dd/MM/yyyy
 * (ej. "25/09/2026") para que sea fácil de leer y de escribir.
 */
data class Tarea(
    val id: Int,
    val nombre: String,
    val materia: String,
    val fechaEntrega: String,
    val completada: Boolean = false
)

/**
 * Lista de tareas guardada en memoria (solo mientras la app está abierta).
 *
 * Es compartida por TareasScreen y CalendarioScreen para que ambas
 * pantallas muestren los mismos datos. Cuando exista una base de datos,
 * solo habrá que reemplazar esta fuente de datos.
 */
object TareasEnMemoria {
    val tareas = mutableStateListOf(
        Tarea(
            id = 1,
            nombre = "Exposición Android Studio",
            materia = "Programación para dispositivos móviles",
            fechaEntrega = "25/09/2026"
        ),
        Tarea(
            id = 2,
            nombre = "Consulta de bases de datos",
            materia = "Bases de datos",
            fechaEntrega = "28/09/2026"
        )
    )

    /** Siguiente id a asignar a una tarea nueva. */
    private var siguienteId = 3

    /** Agrega una tarea nueva a la lista. */
    fun agregarTarea(nombre: String, materia: String, fechaEntrega: String) {
        tareas.add(
            Tarea(
                id = siguienteId,
                nombre = nombre.trim(),
                materia = materia.trim(),
                fechaEntrega = fechaEntrega.trim()
            )
        )
        siguienteId++
    }

    /**
     * Elimina una tarea de la lista por su id. Al quitarla de [tareas],
     * TareasScreen y CalendarioScreen dejan de mostrarla automáticamente.
     */
    fun eliminarTarea(id: Int) {
        tareas.removeAll { it.id == id }
    }

    /** Cambia el estado de una tarea entre pendiente y completada. */
    fun alternarCompletada(id: Int) {
        val posicion = tareas.indexOfFirst { it.id == id }
        if (posicion >= 0) {
            val tarea = tareas[posicion]
            tareas[posicion] = tarea.copy(completada = !tarea.completada)
        }
    }
}

/** Valida que la fecha tenga el formato dd/MM/yyyy y sea una fecha real. */
fun fechaValida(fecha: String): Boolean {
    if (!Regex("""\d{2}/\d{2}/\d{4}""").matches(fecha)) return false
    return try {
        val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        formato.isLenient = false
        formato.parse(fecha) != null
    } catch (e: Exception) {
        false
    }
}

/**
 * Devuelve el día, el mes (0-11) y el año de una fecha válida,
 * o null si la fecha no se puede interpretar.
 */
fun desgloseFecha(fecha: String): Triple<Int, Int, Int>? {
    return try {
        val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        formato.isLenient = false
        val fechaParseada = formato.parse(fecha) ?: return null
        val calendario = Calendar.getInstance()
        calendario.time = fechaParseada
        Triple(
            calendario.get(Calendar.DAY_OF_MONTH),
            calendario.get(Calendar.MONTH),
            calendario.get(Calendar.YEAR)
        )
    } catch (e: Exception) {
        null
    }
}

/**
 * Convierte la fecha dd/MM/yyyy a milisegundos para poder ordenarla.
 * Devuelve 0 si la fecha no es válida.
 */
fun fechaEnMilisegundos(fecha: String): Long {
    return try {
        val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        formato.isLenient = false
        formato.parse(fecha)?.time ?: 0L
    } catch (e: Exception) {
        0L
    }
}

/** Nombres de los meses en español (el índice es el mismo que Calendar.MONTH). */
val mesesEnEspanol = listOf(
    "enero", "febrero", "marzo", "abril", "mayo", "junio",
    "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"
)

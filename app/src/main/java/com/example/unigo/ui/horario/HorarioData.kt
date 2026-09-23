package com.example.unigo.ui.horario

import androidx.compose.runtime.mutableStateListOf
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/** Modelo simple de una clase dentro del horario. */
data class ClaseHorario(
    val id: Int,
    val dia: String,
    val horaInicio: String,
    val horaFin: String,
    val materia: String
)

/** Modelo simple de un día de la semana con sus clases. */
data class DiaHorario(
    val dia: String,
    val clases: List<ClaseHorario>
)

/** Días con clases, en el orden en que se muestran en el horario. */
val diasConClases: List<String> = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes")

/**
 * Horario en memoria (solo mientras la app está abierta).
 *
 * Es compartido por HorarioScreen (donde se crean las clases),
 * CalendarioScreen y HomeScreen para que todas muestren los mismos datos.
 * Cuando exista una base de datos, solo habrá que reemplazar esta fuente.
 */
object HorarioEnMemoria {
    /** Clases de ejemplo con las que arranca el horario (el resto queda vacío). */
    val clases = mutableStateListOf(
        ClaseHorario(
            id = 1,
            dia = "Lunes",
            horaInicio = "7:00 AM",
            horaFin = "9:00 AM",
            materia = "Programación para dispositivos móviles"
        ),
        ClaseHorario(
            id = 2,
            dia = "Miércoles",
            horaInicio = "9:00 AM",
            horaFin = "11:00 AM",
            materia = "Bases de datos"
        )
    )

    /** Siguiente id a asignar a una clase nueva. */
    private var siguienteId = 3

    /** Agrega una clase nueva al horario. */
    fun agregarClase(materia: String, dia: String, horaInicio: String, horaFin: String) {
        clases.add(
            ClaseHorario(
                id = siguienteId,
                dia = dia.trim(),
                horaInicio = horaInicio.trim(),
                horaFin = horaFin.trim(),
                materia = materia.trim()
            )
        )
        siguienteId++
    }

    /**
     * Agrupa las clases por día (Lunes a Viernes) para mostrarlas en el horario.
     * Los días sin clases se devuelven igual, con la lista vacía.
     * Dentro de cada día las clases quedan ordenadas por su hora de inicio.
     */
    fun porDia(): List<DiaHorario> = diasConClases.map { dia ->
        DiaHorario(dia = dia, clases = clasesDe(dia))
    }

    /** Clases de un día concreto (ej. el día de hoy), ordenadas por hora. */
    fun clasesDelDia(dia: String): List<ClaseHorario> = clasesDe(dia)

    private fun clasesDe(dia: String): List<ClaseHorario> =
        clases
            .filter { it.dia.equals(dia, ignoreCase = true) }
            .sortedBy { minutoDeHora(it.horaInicio) ?: Int.MAX_VALUE }
}

/**
 * Convierte una hora como "7:00 AM" en minutos desde la medianoche.
 * Devuelve null si la hora no se puede interpretar.
 */
private fun minutoDeHora(hora: String): Int? {
    val encontrado = Regex("""(\d{1,2}):(\d{2})\s*(AM|PM)?""", RegexOption.IGNORE_CASE)
        .find(hora.trim()) ?: return null
    val horas = encontrado.groupValues[1].toIntOrNull() ?: return null
    val minutos = encontrado.groupValues[2].toIntOrNull() ?: return null
    val sufijo = encontrado.groupValues[3].uppercase(Locale.getDefault())
    val hora24 = when {
        sufijo == "PM" && horas in 1..11 -> horas + 12
        sufijo == "AM" && horas == 12 -> 0
        else -> horas
    }
    if (hora24 !in 0..23 || minutos !in 0..59) return null
    return hora24 * 60 + minutos
}

/**
 * Devuelve la fecha dd/MM/yyyy del próximo día en que se dicta una clase
 * de [dia] (hoy mismo si su horario todavía no pasó), para que el Calendario
 * pueda mostrarla junto a las tareas. Devuelve null si el día no es válido.
 */
fun proximaFechaDeClase(dia: String): String? {
    val indice = diasConClases.indexOfFirst { it.equals(dia, ignoreCase = true) }
    if (indice < 0) return null

    val fecha = Calendar.getInstance()
    val diaDeHoy = when (val diaSemana = fecha.get(Calendar.DAY_OF_WEEK)) {
        Calendar.SUNDAY -> 6
        else -> diaSemana - Calendar.MONDAY
    }
    var diasHasta = indice - diaDeHoy
    if (diasHasta < 0) diasHasta += 7
    fecha.add(Calendar.DAY_OF_MONTH, diasHasta)

    return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(fecha.time)
}

/** Nombres de los días en español indexados por [Calendar.DAY_OF_WEEK]. */
private val diasSemana: Map<Int, String> = mapOf(
    Calendar.MONDAY to "Lunes",
    Calendar.TUESDAY to "Martes",
    Calendar.WEDNESDAY to "Miércoles",
    Calendar.THURSDAY to "Jueves",
    Calendar.FRIDAY to "Viernes",
    Calendar.SATURDAY to "Sábado",
    Calendar.SUNDAY to "Domingo"
)

/** Devuelve el día de la semana actual en español (ej. "Lunes"). */
fun diaActualEnEspanol(): String =
    diasSemana[Calendar.getInstance().get(Calendar.DAY_OF_WEEK)].orEmpty()

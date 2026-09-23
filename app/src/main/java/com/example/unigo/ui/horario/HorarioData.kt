package com.example.unigo.ui.horario

import java.util.Calendar

/** Modelo simple de una clase dentro del horario. */
data class ClaseHorario(
    val horaInicio: String,
    val horaFin: String,
    val materia: String
)

/** Modelo simple de un día de la semana con sus clases. */
data class DiaHorario(
    val dia: String,
    val clases: List<ClaseHorario>
)

/**
 * Horario de ejemplo del estudiante.
 * TODO: reemplazar por los datos reales (BD / API) más adelante.
 */
val horarioEjemplo: List<DiaHorario> = listOf(
    DiaHorario(
        dia = "Lunes",
        clases = listOf(
            ClaseHorario(
                horaInicio = "7:00 AM",
                horaFin = "9:00 AM",
                materia = "Programación para dispositivos móviles"
            ),
            ClaseHorario(
                horaInicio = "9:00 AM",
                horaFin = "11:00 AM",
                materia = "Matemáticas"
            )
        )
    ),
    DiaHorario(
        dia = "Martes",
        clases = listOf(
            ClaseHorario(
                horaInicio = "7:00 AM",
                horaFin = "9:00 AM",
                materia = "Bases de datos"
            ),
            ClaseHorario(
                horaInicio = "9:00 AM",
                horaFin = "11:00 AM",
                materia = "Ingeniería de software"
            )
        )
    ),
    DiaHorario(
        dia = "Miércoles",
        clases = listOf(
            ClaseHorario(
                horaInicio = "7:00 AM",
                horaFin = "9:00 AM",
                materia = "Programación"
            )
        )
    )
)

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

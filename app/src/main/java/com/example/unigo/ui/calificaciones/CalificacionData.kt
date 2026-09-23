package com.example.unigo.ui.calificaciones

import java.util.Locale

/**
 * Nota de un corte de una materia (ej. "Corte 1": 4.2).
 * La escala va de 0.0 a 5.0.
 */
data class NotaCorte(
    val corte: String,
    val nota: Double
)

/**
 * Materia del estudiante con sus notas por corte.
 * El promedio NO se escribe a mano: se calcula con Kotlin
 * sumando las notas y dividiéndolas entre la cantidad de cortes.
 */
data class MateriaCalificacion(
    val nombre: String,
    val notas: List<NotaCorte>
) {
    /** Promedio de la materia. */
    val promedio: Double
        get() = if (notas.isEmpty()) 0.0 else notas.map { it.nota }.average()
}

/** Nota mínima para aprobar en la escala de 0 a 5. */
const val NOTA_MINIMA_APROBADO = 3.0

/**
 * Calificaciones de ejemplo del estudiante (datos locales).
 * TODO: reemplazar por los datos reales (BD / API) más adelante.
 */
val calificacionesEjemplo: List<MateriaCalificacion> = listOf(
    MateriaCalificacion(
        nombre = "Programación para dispositivos móviles",
        notas = listOf(
            NotaCorte(corte = "Corte 1", nota = 4.2),
            NotaCorte(corte = "Corte 2", nota = 4.0),
            NotaCorte(corte = "Corte 3", nota = 4.5)
        )
    ),
    MateriaCalificacion(
        nombre = "Bases de datos",
        notas = listOf(
            NotaCorte(corte = "Corte 1", nota = 3.8),
            NotaCorte(corte = "Corte 2", nota = 4.1),
            NotaCorte(corte = "Corte 3", nota = 4.3)
        )
    )
)

/**
 * Promedio general del estudiante.
 * Se calcula con Kotlin a partir del promedio de cada materia.
 */
fun promedioGeneral(): Double =
    if (calificacionesEjemplo.isEmpty()) {
        0.0
    } else {
        calificacionesEjemplo.map { it.promedio }.average()
    }

/** Formatea una nota con una cifra decimal (ej. 4.0 -> "4.0"). */
fun formatoNota(valor: Double): String = String.format(Locale.US, "%.1f", valor)

/** Formatea un promedio con dos cifras decimales (ej. 4.23 -> "4.23"). */
fun formatoPromedio(valor: Double): String = String.format(Locale.US, "%.2f", valor)

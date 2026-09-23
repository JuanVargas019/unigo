package com.example.unigo.ui.perfil

/**
 * Modelo con la información básica del estudiante.
 *
 * Por ahora se usa una instancia de ejemplo ([Estudiante.ejemplo]).
 * Cuando exista una base de datos o un servicio, solo habrá que
 * reemplazar ese dato por la información real del usuario.
 */
data class Estudiante(
    val nombre: String,
    val correo: String,
    val codigoEstudiantil: String,
    val programa: String,
    val semestre: Int
) {
    companion object {
        /** Datos de ejemplo mientras no hay backend. */
        val ejemplo = Estudiante(
            nombre = "Juan Pérez López",
            correo = "juan.perez@correo.com",
            codigoEstudiantil = "2024123456",
            programa = "Ingeniería de Sistemas",
            semestre = 5
        )
    }
}

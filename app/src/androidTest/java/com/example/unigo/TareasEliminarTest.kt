package com.example.unigo

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.Espresso
import com.example.unigo.ui.tareas.Tarea
import com.example.unigo.ui.tareas.TareasEnMemoria
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Comprueba la eliminación de tareas desde la pantalla de Tareas:
 * - se puede eliminar una tarea de ejemplo,
 * - se puede eliminar una tarea agregada por el usuario,
 * - la tarea eliminada desaparece también del Calendario.
 */
class TareasEliminarTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    /** Las mismas tareas de ejemplo con las que arranca la app. */
    private val tareasDeEjemplo = listOf(
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

    @Before
    fun reiniciarTareas() {
        TareasEnMemoria.tareas.clear()
        TareasEnMemoria.tareas.addAll(tareasDeEjemplo)
    }

    @Test
    fun eliminarTareaDeEjemplo_seQuitaDeTareasYDelCalendario() {
        irATareas()

        // La tarea de ejemplo tiene su ícono de eliminar y se puede pulsar.
        composeRule.onNodeWithContentDescription("Eliminar Exposición Android Studio")
            .performScrollTo()
            .performClick()

        // Confirmación sencilla antes de borrar (mismo patrón que en Horario).
        composeRule.onNodeWithText("¿Eliminar esta tarea?").assertIsDisplayed()
        composeRule.onNodeWithText("Sí, eliminar").performClick()

        // Desaparece de Tareas y la otra tarea de ejemplo sigue intacta.
        composeRule.onNodeWithText("Exposición Android Studio").assertDoesNotExist()
        composeRule.onNodeWithText("Consulta de bases de datos").assertExists()
        assertEquals(0, TareasEnMemoria.tareas.count { it.id == 1 })

        // También desaparece del Calendario.
        composeRule.onNodeWithText("← Volver").performClick()
        composeRule.onNodeWithText("Calendario").performScrollTo().performClick()
        composeRule
            .onNodeWithText("Exposición Android Studio", substring = true)
            .assertDoesNotExist()
        composeRule
            .onNodeWithText("Consulta de bases de datos", substring = true)
            .assertExists()
    }

    @Test
    fun eliminarTareaAgregadaPorElUsuario_seQuitaDeTareasYDelCalendario() {
        irATareas()

        // Se agrega una tarea nueva desde el formulario.
        composeRule.onNodeWithText("+ Nueva tarea").performClick()
        composeRule.onNodeWithText("Nombre de la tarea").performTextInput("Informe de redes")
        composeRule.onNodeWithText("Materia").performTextInput("Redes de computadoras")
        composeRule.onNodeWithText("Fecha de entrega (dd/mm/aaaa)").performTextInput("30/09/2026")
        Espresso.closeSoftKeyboard()
        composeRule.onNodeWithText("Guardar").performClick()

        composeRule.onNodeWithText("Informe de redes").assertIsDisplayed()
        assertEquals(1, TareasEnMemoria.tareas.count { it.nombre == "Informe de redes" })

        // La tarea nueva aparece en el Calendario.
        composeRule.onNodeWithText("← Volver").performClick()
        composeRule.onNodeWithText("Calendario").performScrollTo().performClick()
        composeRule.onNodeWithText("Informe de redes", substring = true).assertExists()

        // Se elimina desde Tareas.
        composeRule.onNodeWithText("← Volver").performClick()
        composeRule.onNodeWithText("Tareas").performScrollTo().performClick()
        composeRule.onNodeWithContentDescription("Eliminar Informe de redes")
            .performScrollTo()
            .performClick()
        composeRule.onNodeWithText("¿Eliminar esta tarea?").assertIsDisplayed()
        composeRule.onNodeWithText("Sí, eliminar").performClick()

        composeRule.onNodeWithText("Informe de redes").assertDoesNotExist()
        assertEquals(0, TareasEnMemoria.tareas.count { it.nombre == "Informe de redes" })

        // Y desaparece del Calendario (las tareas de ejemplo siguen ahí).
        composeRule.onNodeWithText("← Volver").performClick()
        composeRule.onNodeWithText("Calendario").performScrollTo().performClick()
        composeRule.onNodeWithText("Informe de redes", substring = true).assertDoesNotExist()
        composeRule
            .onNodeWithText("Consulta de bases de datos", substring = true)
            .assertExists()
    }

    /** Entra con cualquier usuario y abre la pantalla de Tareas. */
    private fun irATareas() {
        composeRule.onNodeWithText("Usuario o correo").performTextInput("juan")
        composeRule.onNodeWithText("Contraseña").performTextInput("1234")
        Espresso.closeSoftKeyboard()
        composeRule.onNodeWithText("Iniciar sesión").performClick()

        composeRule.onNodeWithText("Tareas").performScrollTo().performClick()
        composeRule.onNodeWithText("Organiza tus entregas del semestre").assertExists()
    }
}

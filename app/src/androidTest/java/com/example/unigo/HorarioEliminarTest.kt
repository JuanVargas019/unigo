package com.example.unigo

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.Espresso
import com.example.unigo.ui.horario.ClaseHorario
import com.example.unigo.ui.horario.HorarioEnMemoria
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Comprueba la eliminación de clases desde el Horario:
 * - se puede eliminar una clase de ejemplo,
 * - se puede eliminar una clase agregada por el usuario,
 * - la clase eliminada desaparece también del Calendario.
 */
class HorarioEliminarTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    /** Las mismas clases de ejemplo con las que arranca la app. */
    private val clasesDeEjemplo = listOf(
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

    @Before
    fun reiniciarHorario() {
        HorarioEnMemoria.clases.clear()
        HorarioEnMemoria.clases.addAll(clasesDeEjemplo)
    }

    @Test
    fun eliminarClaseDeEjemplo_seQuitaDelHorarioYDelCalendario() {
        irAHorario()

        // La clase de ejemplo tiene su ícono de eliminar y se puede pulsar.
        composeRule.onNodeWithContentDescription("Eliminar Programación para dispositivos móviles")
            .performScrollTo()
            .performClick()

        // Confirmación sencilla antes de borrar.
        composeRule.onNodeWithText("¿Eliminar esta clase?").assertIsDisplayed()
        composeRule.onNodeWithText("Sí, eliminar").performClick()

        // Desaparece del horario y la otra clase de ejemplo sigue intacta.
        composeRule.onNodeWithText("Programación para dispositivos móviles").assertDoesNotExist()
        composeRule.onNodeWithText("Bases de datos").assertExists()

        // También desaparece del Calendario (se verifica por el horario de la
        // clase, ya que su materia también aparece como materia de una tarea).
        assertEquals(0, HorarioEnMemoria.clases.count { it.id == 1 })
        composeRule.onNodeWithText("← Volver").performClick()
        composeRule.onNodeWithText("Calendario").performScrollTo().performClick()
        composeRule
            .onNodeWithText("Lunes · 7:00 AM - 9:00 AM", substring = true)
            .assertDoesNotExist()
        composeRule
            .onNodeWithText("Miércoles · 9:00 AM - 11:00 AM", substring = true)
            .assertExists()
    }

    @Test
    fun eliminarClaseAgregadaPorElUsuario_seQuitaDelHorarioYDelCalendario() {
        irAHorario()

        // Se agrega una clase nueva desde el formulario.
        composeRule.onNodeWithText("+ Nueva clase").performClick()
        composeRule.onNodeWithText("Nombre de la materia").performTextInput("Redes de computadoras")
        composeRule.onNodeWithText("Hora de inicio").performTextInput("5:00 PM")
        composeRule.onNodeWithText("Hora de fin").performTextInput("7:00 PM")
        Espresso.closeSoftKeyboard()
        composeRule.onNodeWithText("Guardar").performClick()

        composeRule.onNodeWithText("Redes de computadoras").assertIsDisplayed()
        assertEquals(1, HorarioEnMemoria.clases.count { it.materia == "Redes de computadoras" })

        // La clase nueva aparece en el Calendario.
        composeRule.onNodeWithText("← Volver").performClick()
        composeRule.onNodeWithText("Calendario").performScrollTo().performClick()
        composeRule.onNodeWithText("Redes de computadoras", substring = true).assertExists()

        // Se elimina desde el Horario.
        composeRule.onNodeWithText("← Volver").performClick()
        composeRule.onNodeWithText("Horario").performScrollTo().performClick()
        composeRule.onNodeWithContentDescription("Eliminar Redes de computadoras")
            .performScrollTo()
            .performClick()
        composeRule.onNodeWithText("¿Eliminar esta clase?").assertIsDisplayed()
        composeRule.onNodeWithText("Sí, eliminar").performClick()

        composeRule.onNodeWithText("Redes de computadoras").assertDoesNotExist()
        assertEquals(0, HorarioEnMemoria.clases.count { it.materia == "Redes de computadoras" })

        // Y desaparece del Calendario (las clases de ejemplo siguen ahí;
        // se verifica por su horario para no confundirlas con las materias
        // de las tareas).
        composeRule.onNodeWithText("← Volver").performClick()
        composeRule.onNodeWithText("Calendario").performScrollTo().performClick()
        composeRule
            .onNodeWithText("Lunes · 5:00 PM - 7:00 PM", substring = true)
            .assertDoesNotExist()
        composeRule
            .onNodeWithText("Miércoles · 9:00 AM - 11:00 AM", substring = true)
            .assertExists()
    }

    /** Entra con cualquier usuario y abre la pantalla de Horario. */
    private fun irAHorario() {
        composeRule.onNodeWithText("Usuario o correo").performTextInput("juan")
        composeRule.onNodeWithText("Contraseña").performTextInput("1234")
        Espresso.closeSoftKeyboard()
        composeRule.onNodeWithText("Iniciar sesión").performClick()

        composeRule.onNodeWithText("Horario").performScrollTo().performClick()
        composeRule.onNodeWithText("Horario académico del semestre").assertExists()
    }
}

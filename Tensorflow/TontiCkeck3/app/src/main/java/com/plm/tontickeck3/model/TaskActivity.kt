package com.plm.tontickeck3.model

import java.util.UUID

/**
 * Modelo que representa una actividad dentro de una tarea.
 * @property id Identificador único de la actividad
 * @property description Descripción de la actividad
 * @property isCompleted Indica si la actividad está completada
 */
data class TaskActivity(
    val id: String = UUID.randomUUID().toString(),
    var description: String,
    var isCompleted: Boolean = false
)

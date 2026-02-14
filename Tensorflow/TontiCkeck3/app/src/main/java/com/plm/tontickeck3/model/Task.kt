package com.plm.tontickeck3.model

import java.util.UUID

/**
 * Modelo que representa una tarea con sus actividades asociadas.
 * @property id Identificador único de la tarea
 * @property title Título de la tarea
 * @property description Descripción de la tarea
 * @property createdAt Fecha de creación en milisegundos
 * @property activities Lista de actividades asociadas a la tarea
 */
data class Task(
    val id: String = UUID.randomUUID().toString(),
    var title: String,
    var description: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val activities: MutableList<TaskActivity> = mutableListOf()
)

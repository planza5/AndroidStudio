package com.plm.tontickeck3.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.plm.tontickeck3.data.TaskRepository
import com.plm.tontickeck3.model.Task
import com.plm.tontickeck3.model.TaskActivity

/**
 * ViewModel para gestionar el detalle de una tarea y sus actividades.
 * Maneja las operaciones sobre actividades dentro de una tarea específica.
 */
class TaskDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TaskRepository(application)

    private val _currentTask = MutableLiveData<Task?>()
    val currentTask: LiveData<Task?> get() = _currentTask

    private val _operationResult = MutableLiveData<OperationResult>()
    val operationResult: LiveData<OperationResult> get() = _operationResult

    /**
     * Carga una tarea específica por su ID.
     * @param taskId ID de la tarea a cargar
     */
    fun loadTask(taskId: String) {
        val task = repository.getTaskById(taskId)
        _currentTask.value = task
        if (task == null) {
            _operationResult.value = OperationResult.Error("Tarea no encontrada")
        }
    }

    /**
     * Agrega una nueva actividad a la tarea actual.
     * @param description Descripción de la actividad
     */
    fun addActivity(description: String) {
        if (description.isBlank()) {
            _operationResult.value = OperationResult.Error("La descripción no puede estar vacía")
            return
        }

        val task = _currentTask.value ?: run {
            _operationResult.value = OperationResult.Error("No hay tarea seleccionada")
            return
        }

        val activity = TaskActivity(description = description)
        task.activities.add(activity)

        val success = repository.updateTask(task)
        if (success) {
            _currentTask.value = task // Actualizar LiveData
            _operationResult.value = OperationResult.Success("Actividad agregada correctamente")
        } else {
            _operationResult.value = OperationResult.Error("Error al agregar la actividad")
        }
    }

    /**
     * Elimina una actividad de la tarea actual.
     * @param activityId ID de la actividad a eliminar
     */
    fun deleteActivity(activityId: String) {
        val task = _currentTask.value ?: run {
            _operationResult.value = OperationResult.Error("No hay tarea seleccionada")
            return
        }

        val removed = task.activities.removeIf { it.id == activityId }
        if (removed) {
            val success = repository.updateTask(task)
            if (success) {
                _currentTask.value = task // Actualizar LiveData
                _operationResult.value = OperationResult.Success("Actividad eliminada correctamente")
            } else {
                _operationResult.value = OperationResult.Error("Error al eliminar la actividad")
            }
        } else {
            _operationResult.value = OperationResult.Error("Actividad no encontrada")
        }
    }

    /**
     * Cambia el estado de completado de una actividad.
     * @param activityId ID de la actividad
     * @param isCompleted Nuevo estado de completado
     */
    fun toggleActivityCompletion(activityId: String, isCompleted: Boolean) {
        val task = _currentTask.value ?: return

        val activity = task.activities.find { it.id == activityId }
        if (activity != null) {
            activity.isCompleted = isCompleted
            val success = repository.updateTask(task)
            if (success) {
                _currentTask.value = task // Actualizar LiveData
            }
        }
    }

    /**
     * Actualiza la información de la tarea actual.
     * @param title Nuevo título
     * @param description Nueva descripción
     */
    fun updateTaskInfo(title: String, description: String) {
        if (title.isBlank()) {
            _operationResult.value = OperationResult.Error("El título no puede estar vacío")
            return
        }

        val task = _currentTask.value ?: run {
            _operationResult.value = OperationResult.Error("No hay tarea seleccionada")
            return
        }

        task.title = title
        task.description = description

        val success = repository.updateTask(task)
        if (success) {
            _currentTask.value = task
            _operationResult.value = OperationResult.Success("Tarea actualizada correctamente")
        } else {
            _operationResult.value = OperationResult.Error("Error al actualizar la tarea")
        }
    }

    /**
     * Clase sellada para representar el resultado de operaciones.
     */
    sealed class OperationResult {
        data class Success(val message: String) : OperationResult()
        data class Error(val message: String) : OperationResult()
    }
}

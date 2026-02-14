package com.plm.tontickeck3.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.plm.tontickeck3.data.TaskRepository
import com.plm.tontickeck3.model.Task

/**
 * ViewModel para gestionar la lista de tareas.
 * Maneja las operaciones CRUD de tareas y notifica cambios a la UI.
 */
class TaskListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TaskRepository(application)

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> get() = _tasks

    private val _operationResult = MutableLiveData<OperationResult>()
    val operationResult: LiveData<OperationResult> get() = _operationResult

    init {
        loadTasks()
    }

    /**
     * Carga todas las tareas desde el repositorio.
     */
    fun loadTasks() {
        val taskList = repository.loadTasks()
        _tasks.value = taskList
    }

    /**
     * Agrega una nueva tarea.
     * @param title Título de la tarea
     * @param description Descripción de la tarea
     */
    fun addTask(title: String, description: String) {
        if (title.isBlank()) {
            _operationResult.value = OperationResult.Error("El título no puede estar vacío")
            return
        }

        val task = Task(title = title, description = description)
        val success = repository.addTask(task)

        if (success) {
            loadTasks()
            _operationResult.value = OperationResult.Success("Tarea agregada correctamente")
        } else {
            _operationResult.value = OperationResult.Error("Error al agregar la tarea")
        }
    }

    /**
     * Actualiza una tarea existente.
     * @param task Tarea con los datos actualizados
     */
    fun updateTask(task: Task) {
        val success = repository.updateTask(task)

        if (success) {
            loadTasks()
            _operationResult.value = OperationResult.Success("Tarea actualizada correctamente")
        } else {
            _operationResult.value = OperationResult.Error("Error al actualizar la tarea")
        }
    }

    /**
     * Elimina una tarea.
     * @param taskId ID de la tarea a eliminar
     */
    fun deleteTask(taskId: String) {
        val success = repository.deleteTask(taskId)

        if (success) {
            loadTasks()
            _operationResult.value = OperationResult.Success("Tarea eliminada correctamente")
        } else {
            _operationResult.value = OperationResult.Error("Error al eliminar la tarea")
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

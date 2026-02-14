package com.plm.tontickeck3.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.plm.tontickeck3.model.Task
import java.io.File
import java.io.IOException

/**
 * Repositorio para la persistencia de tareas en formato JSON.
 * Gestiona la lectura y escritura de tareas en el almacenamiento local.
 */
class TaskRepository(private val context: Context) {

    private val gson = Gson()
    private val fileName = "tasks.json"

    /**
     * Carga todas las tareas desde el archivo JSON.
     * @return Lista de tareas o lista vacía si no existe el archivo o hay error
     */
    fun loadTasks(): List<Task> {
        return try {
            val file = File(context.filesDir, fileName)
            if (!file.exists()) {
                return emptyList()
            }

            val jsonString = file.readText()
            if (jsonString.isEmpty()) {
                return emptyList()
            }

            val type = object : TypeToken<List<Task>>() {}.type
            gson.fromJson(jsonString, type) ?: emptyList()
        } catch (e: IOException) {
            e.printStackTrace()
            emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    /**
     * Guarda la lista de tareas en el archivo JSON.
     * @param tasks Lista de tareas a guardar
     * @return true si se guardó correctamente, false en caso contrario
     */
    fun saveTasks(tasks: List<Task>): Boolean {
        return try {
            val jsonString = gson.toJson(tasks)
            val file = File(context.filesDir, fileName)
            file.writeText(jsonString)
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Agrega una nueva tarea a la lista existente.
     * @param task Tarea a agregar
     * @return true si se guardó correctamente
     */
    fun addTask(task: Task): Boolean {
        val tasks = loadTasks().toMutableList()
        tasks.add(task)
        return saveTasks(tasks)
    }

    /**
     * Actualiza una tarea existente.
     * @param task Tarea con los datos actualizados
     * @return true si se actualizó correctamente
     */
    fun updateTask(task: Task): Boolean {
        val tasks = loadTasks().toMutableList()
        val index = tasks.indexOfFirst { it.id == task.id }
        if (index != -1) {
            tasks[index] = task
            return saveTasks(tasks)
        }
        return false
    }

    /**
     * Elimina una tarea por su ID.
     * @param taskId ID de la tarea a eliminar
     * @return true si se eliminó correctamente
     */
    fun deleteTask(taskId: String): Boolean {
        val tasks = loadTasks().toMutableList()
        val removed = tasks.removeIf { it.id == taskId }
        return if (removed) {
            saveTasks(tasks)
        } else {
            false
        }
    }

    /**
     * Obtiene una tarea específica por su ID.
     * @param taskId ID de la tarea a buscar
     * @return La tarea encontrada o null
     */
    fun getTaskById(taskId: String): Task? {
        return loadTasks().find { it.id == taskId }
    }
}

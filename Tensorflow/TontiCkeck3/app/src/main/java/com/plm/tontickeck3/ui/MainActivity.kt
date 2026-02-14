package com.plm.tontickeck3.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.plm.tontickeck3.R
import com.plm.tontickeck3.adapter.TaskAdapter
import com.plm.tontickeck3.model.Task
import com.plm.tontickeck3.viewmodel.TaskListViewModel

/**
 * Activity principal que muestra la lista de tareas.
 * Permite agregar, editar, eliminar y seleccionar tareas.
 */
class MainActivity : AppCompatActivity() {

    private val viewModel: TaskListViewModel by viewModels()
    private lateinit var adapter: TaskAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyStateView: View
    private lateinit var fabAddTask: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()
        setupRecyclerView()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        // Recargar las tareas cuando volvemos a esta activity
        viewModel.loadTasks()
    }

    /**
     * Inicializa las vistas.
     */
    private fun setupViews() {
        recyclerView = findViewById(R.id.recyclerViewTasks)
        emptyStateView = findViewById(R.id.textViewEmptyState)
        fabAddTask = findViewById(R.id.fabAddTask)

        fabAddTask.setOnClickListener {
            showAddTaskDialog()
        }
    }

    /**
     * Configura el RecyclerView con su adapter.
     */
    private fun setupRecyclerView() {
        adapter = TaskAdapter(
            tasks = emptyList(),
            onTaskClick = { task ->
                openTaskDetail(task)
            },
            onTaskEdit = { task ->
                showEditTaskDialog(task)
            },
            onTaskDelete = { task ->
                showDeleteConfirmationDialog(task)
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    /**
     * Observa los cambios en el ViewModel.
     */
    private fun observeViewModel() {
        viewModel.tasks.observe(this) { tasks ->
            adapter.updateTasks(tasks)

            // Mostrar/ocultar estado vacío
            if (tasks.isEmpty()) {
                emptyStateView.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                emptyStateView.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        }

        viewModel.operationResult.observe(this) { result ->
            when (result) {
                is TaskListViewModel.OperationResult.Success -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
                is TaskListViewModel.OperationResult.Error -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    /**
     * Muestra el diálogo para agregar una nueva tarea.
     */
    private fun showAddTaskDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_task, null)
        val editTextTitle = dialogView.findViewById<TextInputEditText>(R.id.editTextTitle)
        val editTextDescription = dialogView.findViewById<TextInputEditText>(R.id.editTextDescription)

        AlertDialog.Builder(this)
            .setTitle("Nueva Tarea")
            .setView(dialogView)
            .setPositiveButton("Agregar") { _, _ ->
                val title = editTextTitle.text.toString()
                val description = editTextDescription.text.toString()
                viewModel.addTask(title, description)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    /**
     * Muestra el diálogo para editar una tarea existente.
     */
    private fun showEditTaskDialog(task: Task) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_task, null)
        val editTextTitle = dialogView.findViewById<TextInputEditText>(R.id.editTextTitle)
        val editTextDescription = dialogView.findViewById<TextInputEditText>(R.id.editTextDescription)

        editTextTitle.setText(task.title)
        editTextDescription.setText(task.description)

        AlertDialog.Builder(this)
            .setTitle("Editar Tarea")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val title = editTextTitle.text.toString()
                val description = editTextDescription.text.toString()

                task.title = title
                task.description = description
                viewModel.updateTask(task)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    /**
     * Muestra un diálogo de confirmación para eliminar una tarea.
     */
    private fun showDeleteConfirmationDialog(task: Task) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Tarea")
            .setMessage("¿Estás seguro de que deseas eliminar \"${task.title}\"?")
            .setPositiveButton("Eliminar") { _, _ ->
                viewModel.deleteTask(task.id)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    /**
     * Abre la pantalla de detalle de una tarea.
     */
    private fun openTaskDetail(task: Task) {
        val intent = Intent(this, TaskDetailActivity::class.java)
        intent.putExtra(TaskDetailActivity.EXTRA_TASK_ID, task.id)
        startActivity(intent)
    }
}

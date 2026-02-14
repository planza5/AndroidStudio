package com.plm.tontickeck3.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.plm.tontickeck3.R
import com.plm.tontickeck3.adapter.ActivityAdapter
import com.plm.tontickeck3.model.TaskActivity
import com.plm.tontickeck3.viewmodel.TaskDetailViewModel

/**
 * Activity que muestra el detalle de una tarea con sus actividades.
 * Permite agregar, eliminar y marcar actividades como completadas.
 */
class TaskDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TASK_ID = "extra_task_id"
    }

    private val viewModel: TaskDetailViewModel by viewModels()
    private lateinit var adapter: ActivityAdapter
    private lateinit var toolbar: MaterialToolbar
    private lateinit var textViewTitle: TextView
    private lateinit var textViewDescription: TextView
    private lateinit var buttonEdit: Button
    private lateinit var buttonDelete: Button
    private lateinit var recyclerViewActivities: RecyclerView
    private lateinit var emptyActivitiesView: TextView
    private lateinit var fabAddActivity: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        setupViews()
        setupRecyclerView()
        observeViewModel()

        // Cargar la tarea
        val taskId = intent.getStringExtra(EXTRA_TASK_ID)
        if (taskId != null) {
            viewModel.loadTask(taskId)
        } else {
            Toast.makeText(this, "Error: ID de tarea no válido", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    /**
     * Inicializa las vistas.
     */
    private fun setupViews() {
        toolbar = findViewById(R.id.toolbar)
        textViewTitle = findViewById(R.id.textViewTaskTitle)
        textViewDescription = findViewById(R.id.textViewTaskDescription)
        buttonEdit = findViewById(R.id.buttonEditTask)
        buttonDelete = findViewById(R.id.buttonDeleteTask)
        recyclerViewActivities = findViewById(R.id.recyclerViewActivities)
        emptyActivitiesView = findViewById(R.id.textViewEmptyActivities)
        fabAddActivity = findViewById(R.id.fabAddActivity)

        // Configurar toolbar
        toolbar.setNavigationOnClickListener {
            finish()
        }

        // Botón para agregar actividad
        fabAddActivity.setOnClickListener {
            showAddActivityDialog()
        }

        // Botón para editar tarea
        buttonEdit.setOnClickListener {
            val task = viewModel.currentTask.value
            if (task != null) {
                showEditTaskDialog(task.title, task.description)
            }
        }

        // Botón para eliminar tarea
        buttonDelete.setOnClickListener {
            showDeleteTaskConfirmation()
        }
    }

    /**
     * Configura el RecyclerView de actividades.
     */
    private fun setupRecyclerView() {
        adapter = ActivityAdapter(
            activities = emptyList(),
            onActivityToggle = { activity, isCompleted ->
                viewModel.toggleActivityCompletion(activity.id, isCompleted)
            },
            onActivityDelete = { activity ->
                showDeleteActivityConfirmation(activity)
            }
        )

        recyclerViewActivities.layoutManager = LinearLayoutManager(this)
        recyclerViewActivities.adapter = adapter
    }

    /**
     * Observa los cambios en el ViewModel.
     */
    private fun observeViewModel() {
        viewModel.currentTask.observe(this) { task ->
            if (task != null) {
                textViewTitle.text = task.title

                if (task.description.isNotBlank()) {
                    textViewDescription.text = task.description
                    textViewDescription.visibility = View.VISIBLE
                } else {
                    textViewDescription.visibility = View.GONE
                }

                adapter.updateActivities(task.activities)

                // Mostrar/ocultar estado vacío
                if (task.activities.isEmpty()) {
                    emptyActivitiesView.visibility = View.VISIBLE
                    recyclerViewActivities.visibility = View.GONE
                } else {
                    emptyActivitiesView.visibility = View.GONE
                    recyclerViewActivities.visibility = View.VISIBLE
                }
            }
        }

        viewModel.operationResult.observe(this) { result ->
            when (result) {
                is TaskDetailViewModel.OperationResult.Success -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
                is TaskDetailViewModel.OperationResult.Error -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    /**
     * Muestra el diálogo para agregar una nueva actividad.
     */
    private fun showAddActivityDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_activity, null)
        val editTextDescription = dialogView.findViewById<TextInputEditText>(R.id.editTextActivityDescription)

        AlertDialog.Builder(this)
            .setTitle("Nueva Actividad")
            .setView(dialogView)
            .setPositiveButton("Agregar") { _, _ ->
                val description = editTextDescription.text.toString()
                viewModel.addActivity(description)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    /**
     * Muestra el diálogo para editar la tarea.
     */
    private fun showEditTaskDialog(currentTitle: String, currentDescription: String) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_task, null)
        val editTextTitle = dialogView.findViewById<TextInputEditText>(R.id.editTextTitle)
        val editTextDescription = dialogView.findViewById<TextInputEditText>(R.id.editTextDescription)

        editTextTitle.setText(currentTitle)
        editTextDescription.setText(currentDescription)

        AlertDialog.Builder(this)
            .setTitle("Editar Tarea")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val title = editTextTitle.text.toString()
                val description = editTextDescription.text.toString()
                viewModel.updateTaskInfo(title, description)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    /**
     * Muestra confirmación para eliminar una actividad.
     */
    private fun showDeleteActivityConfirmation(activity: TaskActivity) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Actividad")
            .setMessage("¿Estás seguro de que deseas eliminar esta actividad?")
            .setPositiveButton("Eliminar") { _, _ ->
                viewModel.deleteActivity(activity.id)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    /**
     * Muestra confirmación para eliminar la tarea completa.
     */
    private fun showDeleteTaskConfirmation() {
        val task = viewModel.currentTask.value ?: return

        AlertDialog.Builder(this)
            .setTitle("Eliminar Tarea")
            .setMessage("¿Estás seguro de que deseas eliminar \"${task.title}\" y todas sus actividades?")
            .setPositiveButton("Eliminar") { _, _ ->
                // Importar TaskRepository para eliminar la tarea
                val repository = com.plm.tontickeck3.data.TaskRepository(this)
                val success = repository.deleteTask(task.id)

                if (success) {
                    Toast.makeText(this, "Tarea eliminada", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Error al eliminar la tarea", Toast.LENGTH_LONG).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}

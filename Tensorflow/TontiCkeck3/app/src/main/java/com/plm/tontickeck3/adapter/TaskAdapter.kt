package com.plm.tontickeck3.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.plm.tontickeck3.R
import com.plm.tontickeck3.model.Task

/**
 * Adapter para mostrar la lista de tareas en un RecyclerView.
 */
class TaskAdapter(
    private var tasks: List<Task>,
    private val onTaskClick: (Task) -> Unit,
    private val onTaskEdit: (Task) -> Unit,
    private val onTaskDelete: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView = itemView.findViewById(R.id.textViewTaskTitle)
        val textViewDescription: TextView = itemView.findViewById(R.id.textViewTaskDescription)
        val textViewActivityCount: TextView = itemView.findViewById(R.id.textViewActivityCount)
        val imageButtonMenu: ImageButton = itemView.findViewById(R.id.imageButtonMenu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]

        holder.textViewTitle.text = task.title

        if (task.description.isNotBlank()) {
            holder.textViewDescription.text = task.description
            holder.textViewDescription.visibility = View.VISIBLE
        } else {
            holder.textViewDescription.visibility = View.GONE
        }

        val activityCount = task.activities.size
        val completedCount = task.activities.count { it.isCompleted }
        holder.textViewActivityCount.text = "$completedCount/$activityCount actividades"

        // Click en la tarjeta completa
        holder.itemView.setOnClickListener {
            onTaskClick(task)
        }

        // Menú de opciones
        holder.imageButtonMenu.setOnClickListener { view ->
            showPopupMenu(view, task)
        }
    }

    override fun getItemCount(): Int = tasks.size

    /**
     * Actualiza la lista de tareas y notifica los cambios.
     */
    fun updateTasks(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }

    /**
     * Muestra un menú popup con opciones de editar y eliminar.
     */
    private fun showPopupMenu(view: View, task: Task) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.inflate(R.menu.task_item_menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_edit -> {
                    onTaskEdit(task)
                    true
                }
                R.id.action_delete -> {
                    onTaskDelete(task)
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }
}

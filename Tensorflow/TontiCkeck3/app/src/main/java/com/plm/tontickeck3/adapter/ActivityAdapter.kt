package com.plm.tontickeck3.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.plm.tontickeck3.R
import com.plm.tontickeck3.model.TaskActivity

/**
 * Adapter para mostrar la lista de actividades de una tarea en un RecyclerView.
 */
class ActivityAdapter(
    private var activities: List<TaskActivity>,
    private val onActivityToggle: (TaskActivity, Boolean) -> Unit,
    private val onActivityDelete: (TaskActivity) -> Unit
) : RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>() {

    class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBoxActivity)
        val imageButtonDelete: ImageButton = itemView.findViewById(R.id.imageButtonDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_activity, parent, false)
        return ActivityViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activity = activities[position]

        holder.checkBox.text = activity.description
        holder.checkBox.isChecked = activity.isCompleted

        // Aplicar estilo tachado si está completada
        if (activity.isCompleted) {
            holder.checkBox.paintFlags = holder.checkBox.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.checkBox.paintFlags = holder.checkBox.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        // Listener para el cambio de estado del checkbox
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            onActivityToggle(activity, isChecked)

            // Actualizar el estilo visual inmediatamente
            if (isChecked) {
                holder.checkBox.paintFlags = holder.checkBox.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                holder.checkBox.paintFlags = holder.checkBox.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }

        // Listener para el botón de eliminar
        holder.imageButtonDelete.setOnClickListener {
            onActivityDelete(activity)
        }
    }

    override fun getItemCount(): Int = activities.size

    /**
     * Actualiza la lista de actividades y notifica los cambios.
     */
    fun updateActivities(newActivities: List<TaskActivity>) {
        activities = newActivities
        notifyDataSetChanged()
    }
}

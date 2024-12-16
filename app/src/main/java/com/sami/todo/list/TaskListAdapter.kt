package com.sami.todo.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sami.todo.databinding.ItemTaskBinding

class TaskListAdapter(val listener: TaskListListener) :
    ListAdapter<Task, TaskListAdapter.TaskViewHolder>(TaskDiffCallback()) {

    private var _binding: ItemTaskBinding? = null
    private val binding get() = _binding!!

    // we use `inner` here to have direct access to the adapter properties
    inner class TaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            // we will display the data here
            binding.taskTitle.text = task.title
            binding.taskDescription.text = task.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        // we create the view here
        _binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(currentList.get(position))
        val deleteButton = binding.deleteTaskButton
        deleteButton.setOnClickListener {
            listener.onClickDelete(currentList.get(position))
        }
        val editButton = binding.editTaskButton
        editButton.setOnClickListener {
            listener.onClickEdit(currentList.get(position))
        }
    }

}

class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }
}
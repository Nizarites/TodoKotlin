package com.sami.todo.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.sami.todo.R

class TaskListFragment : Fragment() {
    private var taskList = listOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )
    private val adapter = TaskListAdapter()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_task_list, container,false)
        adapter.currentList = taskList
        return rootView
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // on utilise `requireView()` pour éviter les erreurs de `null`
        val recyclerView = requireView().findViewById<RecyclerView>(R.id.task_list)
        recyclerView.adapter = adapter
    }
}
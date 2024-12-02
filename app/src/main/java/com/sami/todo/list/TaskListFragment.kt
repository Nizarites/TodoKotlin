package com.sami.todo.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.sami.todo.databinding.FragmentTaskListBinding
import com.sami.todo.detail.DetailActivity

class TaskListFragment : Fragment() {
    private var taskList = listOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )
    private val adapter = TaskListAdapter()
    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    // dans cette callback on recupera la task et on l'ajoutera à la liste
    val createTask =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            val data: Intent? = result.data
//            val task = data?.getParcelableExtra<Task>("task")
//            task?.let {
//                taskList = taskList + it
//                adapter.submitList(taskList)
//            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        val rootView = binding.root
        // val rootView = inflater.inflate(R.layout.fragment_task_list, container,false)
        adapter.submitList(taskList)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // on utilise `requireView()` pour éviter les erreurs de `null`
        val recyclerView = binding.taskList
        recyclerView.adapter = adapter
        val addButton = binding.addTaskButton
        addButton.setOnClickListener {
            /*val newTask =
                Task(id = UUID.randomUUID().toString(), title = "Task ${taskList.size + 1}")
            taskList = taskList + newTask
            adapter.submitList(taskList)*/
            val intent = Intent(context, DetailActivity::class.java)
            createTask.launch(intent)
        }

        adapter.onClickDelete = { task ->
            taskList = taskList.filter { it.id != task.id }
            adapter.submitList(taskList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
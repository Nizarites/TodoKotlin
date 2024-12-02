package com.sami.todo.list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.sami.todo.databinding.FragmentTaskListBinding
import com.sami.todo.detail.DetailActivity
import java.util.UUID

class TaskListFragment : Fragment() {
    private var taskList = listOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )
    val adapterListener : TaskListListener = object : TaskListListener {

        override fun onClickDelete(task: Task) {
            taskList = taskList.filter { it.id != task.id }
            adapter.submitList(taskList)}

        override fun onClickEdit(task: Task) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(TASK_KEY, task)
            editTask.launch(intent)}
    }
    val adapter = TaskListAdapter(adapterListener)
    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!
    companion object {
        const val TASK_KEY = "task"
    }
    // dans cette callback on recupera la task et on l'ajoutera à la liste
    val createTask =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = result.data?.getSerializableExtra(TASK_KEY) as Task?
                taskList = taskList + task!!
                adapter.submitList(taskList)
            }
        }
    val editTask =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = result.data?.getSerializableExtra(TASK_KEY) as Task?
                taskList = taskList.map {
                    if (it.id == task?.id) {
                        task
                    } else { // sinon on garde l'ancienne valeur
                        it
                    }
                }
                adapter.submitList(taskList)
            }
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
        val intent = Intent(context, DetailActivity::class.java)

        addButton.setOnClickListener {
            createTask.launch(intent)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
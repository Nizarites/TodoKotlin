package com.sami.todo.list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil3.load
import coil3.request.error
import com.sami.todo.R
import com.sami.todo.data.Api
import com.sami.todo.data.TaskListViewModel
import com.sami.todo.databinding.FragmentTaskListBinding
import com.sami.todo.detail.DetailActivity
import com.sami.todo.user.UserActivity
import kotlinx.coroutines.launch

class TaskListFragment : Fragment() {
    val adapterListener: TaskListListener = object : TaskListListener {

        override fun onClickDelete(task: Task) {
            viewModel.remove(task)
        }

        override fun onClickEdit(task: Task) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(TASK_KEY, task)
            editTask.launch(intent)
        }
    }
    val adapter = TaskListAdapter(adapterListener)
    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val TASK_KEY = "task"
    }

    private val viewModel: TaskListViewModel by viewModels()

    // dans cette callback on recupera la task et on l'ajoutera à la liste
    val createTask =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.add(result.data?.getSerializableExtra(TASK_KEY) as Task)
            }
        }
    val editTask =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.edit(result.data?.getSerializableExtra(TASK_KEY) as Task)
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
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // on utilise `requireView()` pour éviter les erreurs de `null`
        val recyclerView = binding.taskList
        recyclerView.adapter = adapter
        val addButton = binding.addTaskButton
        val userIcone = binding.userIcone
        val intent = Intent(context, DetailActivity::class.java)

        addButton.setOnClickListener {
            createTask.launch(intent)
        }

        userIcone.setOnClickListener{
            startActivity(Intent(context, UserActivity::class.java))
        }



        lifecycleScope.launch { // on lance une coroutine car `collect` est `suspend`
            viewModel.tasksStateFlow.collect { newList ->
                // cette lambda est exécutée à chaque fois que la liste est mise à jour dans le VM
                // -> ici, on met à jour la liste dans l'adapter
                adapter.submitList(newList)
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
        // Ici on ne va pas gérer les cas d'erreur donc on force le crash avec "!!"
        lifecycleScope.launch {
            val user = Api.userWebService.fetchUser().body()
            Log.e("User", user?.name.toString())
            binding.userTextView.text = user?.name
            binding.userIcone.load(user?.avatar) {
                error(R.drawable.ic_launcher_background)
            }
        }
    }
}
package com.sami.todo.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sami.todo.list.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TaskListViewModel : ViewModel() {
    private val webService = Api.tasksWebService

    public val tasksStateFlow = MutableStateFlow<List<Task>>(emptyList())

    fun refresh() {
        viewModelScope.launch {
            val response = webService.fetchTasks() // Call HTTP
            if (!response.isSuccessful) {
                Log.e("Network", "Error: ${response.message()}")
                return@launch
            }
            val fetchedTasks = response.body()!!
            tasksStateFlow.value =
                fetchedTasks
        }
    }

    fun add(task: Task) {
        viewModelScope.launch {
            val response = webService.create(task)
            if (!response.isSuccessful) {
                Log.e("Network", "Error: ${response.raw()}")
                return@launch
            }

            val newTask = response.body()!!
            val updatedList = tasksStateFlow.value + newTask
            tasksStateFlow.value = updatedList
        }
    }

    fun edit(task: Task) {
        viewModelScope.launch {
            val response = webService.update(task)
            if (!response.isSuccessful) {
                Log.e("Network", "Error: ${response.raw()}")
                return@launch
            }
            val newTask = response.body()!!
            val updatedList = tasksStateFlow.value.map {
                if (it.id == newTask.id) newTask else it
            }
            tasksStateFlow.value = updatedList
        }
    }

    fun remove(task: Task) {
        viewModelScope.launch {
            val response = webService.delete(id = task.id)
            if (!response.isSuccessful) {
                Log.e("Network", "Error: ${response.raw()}")
                return@launch
            }

            val updatedList = tasksStateFlow.value.filter { it.id != task.id }
            tasksStateFlow.value = updatedList
        }
    }
}
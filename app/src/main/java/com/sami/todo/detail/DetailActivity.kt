package com.sami.todo.detail

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sami.todo.detail.ui.theme.TodoSamiTheme
import com.sami.todo.list.Task
import com.sami.todo.list.TaskListFragment.Companion.TASK_KEY
import java.util.UUID

class DetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val task = when (intent?.action) {
            Intent.ACTION_SEND -> {
                // Share case : create new task with shared text as desc
                val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT) ?: ""
                Task(id = UUID.randomUUID().toString(),
                    title = "", // Empty title to fill by user
                    description = sharedText
                )
            }
            else -> {
                // Normal case : retrieve task from intent
                intent.getSerializableExtra("task") as? Task
            }
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoSamiTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Detail(
                        modifier = Modifier.padding(innerPadding),
                        onValidate = { task ->
                            intent.putExtra(TASK_KEY, task)
                            setResult(RESULT_OK, intent)
                            finish()
                        },
                        task = task

                    )
                }
            }
        }
    }

}

@Composable
fun Detail(modifier: Modifier = Modifier,onValidate: (Task) -> Unit, task: Task? = null) {
    var newTask by remember { mutableStateOf(task?: Task(id = UUID.randomUUID().toString(), title = "New Task !")) }

    Column (modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Task detail",
            style = MaterialTheme.typography.headlineLarge,
            modifier = modifier
        )

        OutlinedTextField(
            value = newTask.title,
            onValueChange = {newTask = newTask.copy(title = it)},
            label = { Text("Task title") },
            textStyle = MaterialTheme.typography.headlineLarge,
            modifier = modifier
        )

        OutlinedTextField(
            value = newTask.description,
            onValueChange = {newTask = newTask.copy(description = it)},
            label = { Text("Description") },
            textStyle = MaterialTheme.typography.headlineLarge,
            modifier = modifier
        )
        Button (
            onClick = {
                onValidate(newTask)
            },
            modifier = modifier
        ) {
            Text("Validate")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailPreview() {
    TodoSamiTheme {
    }
}

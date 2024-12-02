package com.sami.todo.detail

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sami.todo.detail.ui.theme.TodoSamiTheme
import com.sami.todo.list.Task
import java.util.UUID

class DetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoSamiTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Detail(
                        modifier = Modifier.padding(innerPadding),
                        onValidate = { task ->
                            intent.putExtra("task", task)
                            setResult(RESULT_OK, intent)
                            finish()
                        }
                    )
                }
            }
        }
    }

}

@Composable
fun Detail(modifier: Modifier = Modifier,onValidate: (Task) -> Unit) {

    Column (modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Task detail",
            style = MaterialTheme.typography.headlineLarge,
            modifier = modifier
        )
        Text(
            text = "Title",
            style = MaterialTheme.typography.headlineLarge,
            modifier = modifier
        )
        Text(
            text = "Description",
            style = MaterialTheme.typography.headlineLarge,
            modifier = modifier
        )
        Button (
            onClick = {
                val newTask = Task(id = UUID.randomUUID().toString(), title = "New Task !")
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
        Detail() { }
    }
}
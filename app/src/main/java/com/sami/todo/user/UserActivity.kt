package com.sami.todo.user

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import coil3.load
import coil3.request.error
import com.sami.todo.R
import com.sami.todo.data.Api
import com.sami.todo.user.ui.UserViewModel
import com.sami.todo.user.ui.theme.TodoSamiTheme
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UserActivity : ComponentActivity() {

    private val captureUri by lazy {
        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var bitmap: Bitmap? by remember { mutableStateOf(null) }
            var uri: Uri? by remember { mutableStateOf(null) }
            val composeScope = rememberCoroutineScope()
            val takePicture = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                if (success) uri = captureUri
                finish()

            }
            val pickPhoto = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
                uri = it
                val userViewModel: UserViewModel by viewModels()
                uri?.let { userViewModel.updateAvatar(it, this@UserActivity) }
                finish()
            }
            val oldAndroid = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission())
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q || it) {
                    pickPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                } else {
                    Log.e("UserActivity", "Permission denied")
                }
            }
            Column {
                AsyncImage(
                    modifier = Modifier.fillMaxHeight(.2f),
                    model = bitmap ?: uri,
                    contentDescription = null
                )
                Button(
                    onClick = {
                        captureUri?.let { takePicture.launch(it) }
                    },
                    content = { Text("Take picture") }
                )
                Button(
                    onClick = {
                        oldAndroid.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    },
                    content = { Text("Pick photo") }
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TodoSamiTheme {
        Greeting("Android")
    }
}
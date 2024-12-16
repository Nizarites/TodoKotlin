package com.sami.todo.user.ui

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sami.todo.data.Api
import com.sami.todo.user.Arg
import com.sami.todo.user.BodyUpdate
import com.sami.todo.user.Commands
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class UserViewModel : ViewModel() {

    fun updateAvatar(uri: Uri, context: Context) {
        viewModelScope.launch {
            val avatarPart = uri.toRequestBody(context)
            Api.userWebService.updateAvatar(avatarPart)
        }
    }

    private fun Uri.toRequestBody(context: Context): MultipartBody.Part {
        val fileInputStream = context.contentResolver.openInputStream(this)!!
        val fileBody = fileInputStream.readBytes().toRequestBody()
        return MultipartBody.Part.createFormData(
            name = "avatar",
            filename = "avatar.jpg",
            body = fileBody
        )
    }

    fun update(userName: String) {
        viewModelScope.launch {
            val command = Commands(args = Arg(name = userName))
            val response =
                Api.userWebService.update(BodyUpdate(listOf(command))) // Complete the network call
            if (!response.isSuccessful) {
                Log.e("Network", "Error: ${response.raw()}")
                Log.e("Error", "Error ${response.errorBody().toString()}")
                return@launch
            }
        }
    }
}
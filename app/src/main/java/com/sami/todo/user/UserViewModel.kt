package com.sami.todo.user.ui

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sami.todo.data.Api
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
}
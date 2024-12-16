package com.sami.todo.data

import com.sami.todo.user.BodyUpdate
import com.sami.todo.user.Commands
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UserWebService {
    @GET("/sync/v9/user/")
    suspend fun fetchUser(): Response<User>

    @Multipart
    @POST("sync/v9/update_avatar")
    suspend fun updateAvatar(@Part avatar: MultipartBody.Part): Response<User>

    @POST("sync/v9/sync")
    suspend fun update(@Body commands: BodyUpdate): Response<Unit>
}
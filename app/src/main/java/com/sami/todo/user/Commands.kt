package com.sami.todo.user

import com.sami.todo.data.User
import kotlinx.serialization.SerialName
import java.util.UUID

// Objet Commands de type user_update
@kotlinx.serialization.Serializable
data class Commands(
    @SerialName("type")
    val type : String = "user_update",
    @SerialName("args")
    val args : List<Arg>,
    @SerialName("uuid")
    val uuid : String= UUID.randomUUID().toString(),
)

@kotlinx.serialization.Serializable
data class Arg(
    @SerialName("full_name")
    val name : String,
)

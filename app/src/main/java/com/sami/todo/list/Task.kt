package com.sami.todo.list

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Task(
    @SerialName("id")
    var id: String,
    @SerialName("content")
    var title: String,
    @SerialName("description")
    var description: String = "Default"
) : Serializable



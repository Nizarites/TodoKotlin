package com.sami.todo.user

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable

data class BodyUpdate(
    @SerialName("commands")
    val commands: List<Commands>,
)

package com.sami.todo.list

import java.io.Serializable

@kotlinx.serialization.Serializable
data class Task(var id: String,var title: String,var description: String="Default") : Serializable



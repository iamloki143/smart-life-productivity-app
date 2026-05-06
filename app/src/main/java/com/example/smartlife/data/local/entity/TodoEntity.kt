package com.example.smartlife.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TodoEntity (
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val title: String,
    val description: String,
    val type: String,
    val isDone: Boolean

)
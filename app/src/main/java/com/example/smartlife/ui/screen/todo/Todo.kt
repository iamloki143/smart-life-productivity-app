package com.example.smartlife.ui.screen.todo

data class Todo (
    val id:Int,
    val title:String,
    val description:String,
    val type: String,
    val isDone: Boolean=false
)
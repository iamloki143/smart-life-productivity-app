package com.example.smartlife.viewmodel.todoViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartlife.data.local.dao.TodoDao
import com.example.smartlife.data.local.entity.TodoEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TodoViewModel(private val dao: TodoDao): ViewModel() {
    val todo=dao.getAllTodos()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
    fun addTodo(todo: TodoEntity){
        viewModelScope.launch {
            dao.insert(todo)
        }
    }
    fun updateTodo(todo: TodoEntity){
        viewModelScope.launch{
            dao.update(todo)
        }
    }
    fun deleteTodo(todo: TodoEntity){
        viewModelScope.launch {
            dao.delete(todo)
        }
    }
}
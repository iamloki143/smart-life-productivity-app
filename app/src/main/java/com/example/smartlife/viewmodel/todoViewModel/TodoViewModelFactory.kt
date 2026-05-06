package com.example.smartlife.viewmodel.todoViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.smartlife.data.local.dao.TodoDao

class TodoViewModelFactory(
    private val dao: TodoDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TodoViewModel(dao) as T
    }
}
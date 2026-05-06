package com.example.smartlife.viewmodel.journalviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.smartlife.data.local.dao.JournalDao

class JournalViewModelFactory(
    private val dao: JournalDao
): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>):T{
        return JournalViewModel(dao) as T
    }
}
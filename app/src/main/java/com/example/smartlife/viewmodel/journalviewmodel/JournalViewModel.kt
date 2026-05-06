package com.example.smartlife.viewmodel.journalviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartlife.data.local.dao.JournalDao
import com.example.smartlife.data.local.entity.JournalEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class JournalViewModel(private val dao: JournalDao): ViewModel() {
    fun getEntry(date: String)=
        dao.getEntry(date).stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )
    fun saveEntry(date: String,text:String,images: String){
        viewModelScope.launch {
            dao.insert(JournalEntity(date,text,images))
        }
    }
    fun getAllEntryDates(): Flow<List<String>>{
        return dao.getAllDates()
    }
}
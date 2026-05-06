package com.example.smartlife.data.local.appdatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.smartlife.data.local.dao.JournalDao
import com.example.smartlife.data.local.dao.TodoDao
import com.example.smartlife.data.local.entity.JournalEntity
import com.example.smartlife.data.local.entity.TodoEntity

@Database(entities = [TodoEntity::class, JournalEntity::class], version = 2)
abstract class AppDatabase: RoomDatabase() {
    abstract fun todoDao(): TodoDao
    abstract fun journalDao(): JournalDao
}
package com.example.smartlife.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.smartlife.data.local.entity.JournalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: JournalEntity)

    @Query("SELECT * FROM JournalEntity WHERE date = :date")
    fun getEntry(date: String): Flow<JournalEntity?>
    @Query("SELECT date FROM JournalEntity")
    fun getAllDates(): Flow<List<String>>
}
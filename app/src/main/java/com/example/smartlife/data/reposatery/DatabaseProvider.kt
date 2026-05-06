package com.example.smartlife.data.reposatery

import android.content.Context
import androidx.room.Room
import com.example.smartlife.data.local.appdatabase.AppDatabase

object DatabaseProvider {
    private var INSTANCE: AppDatabase?=null
    fun getDB(context: Context): AppDatabase{
        return INSTANCE?: synchronized(this){
            val db= Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "smart_life_db"
            )
                .fallbackToDestructiveMigration(true)
                .build()
            INSTANCE=db
            db
        }
    }
}
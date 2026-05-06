package com.example.smartlife.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class JournalEntity (
    @PrimaryKey
    val date: String,
    val content:String,
    val images: String
)
package com.kaspersky.data.storage.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "translations")
data class TranslationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val query: String,
    val translation: String,
    var favorite : Boolean = false
)
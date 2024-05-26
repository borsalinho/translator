package com.kaspersky.data.storage.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kaspersky.data.storage.dao.TranslationDao
import com.kaspersky.data.storage.model.TranslationEntity

@Database(entities = [TranslationEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun translationDao(): TranslationDao
}
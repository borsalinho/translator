package com.kaspersky.data.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.kaspersky.data.storage.model.TranslationEntity

@Dao
interface TranslationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(translation: TranslationEntity)

    @androidx.room.Query("SELECT * FROM translations ORDER BY id DESC LIMIT :batchSize OFFSET :index")
    suspend fun getTranslations(index: Int, batchSize: Int): List<TranslationEntity>

    @androidx.room.Query("SELECT * FROM translations WHERE favorite = 1 ORDER BY id DESC")
    suspend fun getFavoriteTranslations(): List<TranslationEntity>

    @androidx.room.Query("UPDATE translations SET favorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Int, isFavorite: Boolean)

}
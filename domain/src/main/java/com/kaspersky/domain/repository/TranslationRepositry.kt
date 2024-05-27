package com.kaspersky.domain.repository


import com.kaspersky.domain.model.WordTranslation

interface TranslationRepositry {
    suspend fun loadHistoryFromDB(index: Int, currentIndex: Int) : List<WordTranslation>

    suspend fun updateFavoriteItem(id: Int, isFavorite: Boolean)
}
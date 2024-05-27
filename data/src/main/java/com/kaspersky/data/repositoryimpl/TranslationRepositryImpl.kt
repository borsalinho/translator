package com.kaspersky.data.repositoryimpl

import com.kaspersky.data.mappers.toWordTranslation
import com.kaspersky.data.storage.dao.TranslationDao


import com.kaspersky.domain.model.WordTranslation
import com.kaspersky.domain.repository.TranslationRepositry

class TranslationRepositryImpl(
    var translationDao : TranslationDao
) : TranslationRepositry {

    override suspend fun loadHistoryFromDB(index: Int, currentIndex: Int) : List<WordTranslation> {
        val history = translationDao.getTranslations(index = index, batchSize = currentIndex)
        return history.map { it.toWordTranslation() }
    }

    override suspend fun updateFavoriteItem(id: Int, isFavorite: Boolean) {
        translationDao.updateFavoriteItem(id = id, isFavorite = isFavorite)
    }

    override suspend fun getFavoriteTranslations(index: Int, currentIndex: Int) : List<WordTranslation> {
        val favoritesHistory =  translationDao.getFavoriteTranslations(index = index, batchSize = currentIndex)
        return favoritesHistory.map { it.toWordTranslation() }
    }
}
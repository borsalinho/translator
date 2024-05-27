package com.kaspersky.data.repositoryimpl

import com.kaspersky.data.mappers.toWordTranslation
import com.kaspersky.data.storage.dao.TranslationDao
import com.kaspersky.data.storage.model.TranslationEntity

import com.kaspersky.domain.model.WordTranslation
import com.kaspersky.domain.repository.TranslationRepositry

class TranslationRepositryImpl(
    var translationDao : TranslationDao
) : TranslationRepositry {

    override suspend fun loadHistoryFromDB(): List<WordTranslation> {
        val history = translationDao.getTranslations()
        return history.map { it.toWordTranslation() }
    }
}
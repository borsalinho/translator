package com.kaspersky.data.repositoryimpl

import com.kaspersky.data.network.ApiSkyEnd
import com.kaspersky.data.storage.dao.TranslationDao
import com.kaspersky.data.storage.model.TranslationEntity

import com.kaspersky.domain.model.WordQuerry
import com.kaspersky.domain.model.WordsResponce
import com.kaspersky.domain.repository.WordQuerryRepository

class WordQuerryRepositryImpl(
    var apiSkyEng : ApiSkyEnd,
    var translationDao : TranslationDao
) : WordQuerryRepository {
    override suspend fun SaveWordQuerryToDataBase(querry: WordQuerry, responce: WordsResponce){
        translationDao.insert(TranslationEntity(0,querry.word_querry,responce.responce))
    }

    override suspend fun SendWordQuerryToApiAndGetResponse(wordQuerry : WordQuerry) : WordsResponce {

        val result = apiSkyEng.getTranslate(wordQuerry.word_querry)
        var translations = ""

        if (result.isNotEmpty()) {
            translations = result.flatMap { it.meanings }
                .take(3)
                .map { it.translation.text }
                .joinToString(", ")
        } else {
            throw IllegalArgumentException("Нет перевода")
        }

        return WordsResponce(translations)
    }
}
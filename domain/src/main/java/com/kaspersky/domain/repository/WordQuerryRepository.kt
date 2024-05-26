package com.kaspersky.domain.repository

import com.kaspersky.domain.model.WordQuerry
import com.kaspersky.domain.model.WordsResponce

interface WordQuerryRepository {
    suspend fun SendWordQuerryToApiAndGetResponse(wordQuerry : WordQuerry) : WordsResponce

    suspend fun SaveWordQuerryToDataBase(query : WordQuerry, responce: WordsResponce)
}

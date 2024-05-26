package com.kaspersky.domain.usecases

import com.kaspersky.domain.model.WordQuerry
import com.kaspersky.domain.model.WordsResponce
import com.kaspersky.domain.repository.WordQuerryRepository

class SaveWordToDBUseCase(private val wordQuerryRepository: WordQuerryRepository) {
    suspend fun execute(query: WordQuerry, responce : WordsResponce ) {
        return wordQuerryRepository.SaveWordQuerryToDataBase(query = query, responce = responce)
    }
}
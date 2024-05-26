package com.kaspersky.domain.usecases

import com.kaspersky.domain.model.WordQuerry
import com.kaspersky.domain.model.WordsResponce
import com.kaspersky.domain.repository.WordQuerryRepository

class SendWordUseCase(private val wordQuerryRepository: WordQuerryRepository){
    suspend fun execute(query: WordQuerry) : WordsResponce{
        return wordQuerryRepository.SendWordQuerryToApiAndGetResponse(wordQuerry = query)
    }
}
package com.kaspersky.domain.usecases

import com.kaspersky.domain.model.WordTranslation
import com.kaspersky.domain.repository.TranslationRepositry

class GetTranslaionUseCase(private val translationRepositry : TranslationRepositry) {

    suspend fun execute() : List<WordTranslation>{
        return translationRepositry.loadHistoryFromDB()
    }
}
package com.kaspersky.domain.usecases


import com.kaspersky.domain.model.WordTranslation
import com.kaspersky.domain.repository.TranslationRepositry

class GetTranslaionUseCase(private val translationRepositry : TranslationRepositry) {

    suspend fun execute(index: Int, currentIndex: Int) : List<WordTranslation>{
        return translationRepositry.loadHistoryFromDB(index = index, currentIndex = currentIndex)
    }
}
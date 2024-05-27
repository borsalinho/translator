package com.kaspersky.domain.usecases

import com.kaspersky.domain.model.WordTranslation
import com.kaspersky.domain.repository.TranslationRepositry

class GetFavoriteTranslationsUseCase(private val translationRepositry : TranslationRepositry) {
    suspend fun execute(index: Int, currentIndex: Int) : List<WordTranslation>{
        return translationRepositry.getFavoriteTranslations(index = index, currentIndex = currentIndex)
    }
}
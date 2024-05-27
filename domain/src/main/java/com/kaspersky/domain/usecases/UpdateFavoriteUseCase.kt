package com.kaspersky.domain.usecases

import com.kaspersky.domain.repository.TranslationRepositry

class UpdateFavoriteUseCase(private val translationRepositry : TranslationRepositry) {
    suspend fun execute(id: Int, isFavorite: Boolean){
        translationRepositry.updateFavoriteItem(id = id, isFavorite = isFavorite)
    }
}
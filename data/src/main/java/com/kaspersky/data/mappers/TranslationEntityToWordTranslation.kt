package com.kaspersky.data.mappers

import com.kaspersky.data.storage.model.TranslationEntity
import com.kaspersky.domain.model.WordTranslation

fun TranslationEntity.toWordTranslation() : WordTranslation {
    return WordTranslation(
        id = this.id,
        query = this.query,
        translation = this.translation,
        favorite = this.favorite
    )
}
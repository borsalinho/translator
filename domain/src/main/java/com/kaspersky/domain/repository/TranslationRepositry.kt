package com.kaspersky.domain.repository


import com.kaspersky.domain.model.WordTranslation

interface TranslationRepositry {
    suspend fun loadHistoryFromDB() : List<WordTranslation>
}
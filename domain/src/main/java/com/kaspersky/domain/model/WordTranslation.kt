package com.kaspersky.domain.model

data class WordTranslation(
    val id: Int,
    val query: String,
    val translation: String,
    var favorite : Boolean = false
)

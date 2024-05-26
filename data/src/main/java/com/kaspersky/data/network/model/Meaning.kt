package com.kaspersky.data.network.model

data class Meaning(
    val id: Int,
    val partOfSpeechCode: String,
    val translation: Translation,
    val previewUrl: String?,
    val imageUrl: String?,
    val transcription: String,
    val soundUrl: String
)

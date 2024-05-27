package com.kaspersky.translator.view_model


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope


import com.kaspersky.domain.model.WordQuerry
import com.kaspersky.domain.model.WordTranslation
import com.kaspersky.domain.model.WordsResponce
import com.kaspersky.domain.repository.TranslationRepositry
import com.kaspersky.domain.usecases.GetTranslaionUseCase
import com.kaspersky.domain.usecases.SaveWordToDBUseCase
import com.kaspersky.domain.usecases.SendWordUseCase
import com.kaspersky.domain.usecases.UpdateFavoriteUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyViewModel(
    private var sendWordUseCase: SendWordUseCase,
    private var saveWordToDBUseCase: SaveWordToDBUseCase,
    private var getTranslaionUseCase: GetTranslaionUseCase,
    private var updateFavoriteUseCase: UpdateFavoriteUseCase,
    private var translationRepositry: TranslationRepositry
) : ViewModel() {

    var currentIndex = 10
    val batchSize = 10

    var currentIndexFavorite = 10
    val batchSizeFavorite = 10

    init {
        viewModelScope.launch {
            loadHistory()
        }
    }

    private val _translations = MutableLiveData<List<WordTranslation>>()
    val translations: LiveData<List<WordTranslation>> get() = _translations

    private val _favorite_translations = MutableLiveData<List<WordTranslation>>()
    val favoriteTranslations: LiveData<List<WordTranslation>> get() = _favorite_translations

    private var _text_result = MutableLiveData<String>()
    val text_result: LiveData<String> get() = _text_result

    fun setTextResult(value: String) {
        _text_result.value = value
    }


    suspend fun sendWordToApi(query: WordQuerry): WordsResponce {
        return withContext(Dispatchers.IO) {
            sendWordUseCase.execute(query = query)
        }
    }

    suspend fun saveTranslation(
        query: WordQuerry,
        responce: WordsResponce
    ) {
        withContext(Dispatchers.IO) {
            saveWordToDBUseCase.execute(query = query, responce = responce)
        }
    }

    suspend fun loadHistory() {
        val history = getTranslaionUseCase.execute(index = 0, currentIndex = currentIndex)
        _translations.postValue(history)
    }

    suspend fun updateFavorite(id: Int, isFavorite: Boolean) {
        updateFavoriteUseCase.execute(id = id, isFavorite = isFavorite)
        loadHistory()
    }

    suspend fun loadFavoriteHistory() {
        val favoriteHistory = translationRepositry.getFavoriteTranslations(
            index = 0,
            currentIndex = currentIndexFavorite
        )
        _favorite_translations.postValue(favoriteHistory)
    }

}
package com.kaspersky.translator.view_model


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope


import com.kaspersky.domain.model.WordQuerry
import com.kaspersky.domain.model.WordTranslation
import com.kaspersky.domain.model.WordsResponce
import com.kaspersky.domain.usecases.GetTranslaionUseCase
import com.kaspersky.domain.usecases.SaveWordToDBUseCase
import com.kaspersky.domain.usecases.SendWordUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyViewModel(
    private var sendWordUseCase : SendWordUseCase,
    private var saveWordToDBUseCase : SaveWordToDBUseCase,
    private var getTranslaionUseCase : GetTranslaionUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            loadHistory()
        }
    }


    private val _translations = MutableLiveData<List<WordTranslation>>()
    val translations: LiveData<List<WordTranslation>> get() = _translations

    private var _text_result = MutableLiveData<String>()
    val text_result: LiveData<String> get() = _text_result

    fun setTextResult(value: String) {
        _text_result.value = value
    }



    suspend fun sendWordToApi(query : WordQuerry) : WordsResponce {
        return withContext(Dispatchers.IO){
            sendWordUseCase.execute(query = query)
        }
    }

    suspend fun saveTranslation(
        query : WordQuerry,
        responce : WordsResponce){
        withContext(Dispatchers.IO){
            saveWordToDBUseCase.execute(query = query, responce = responce)
        }
    }

    suspend fun loadHistory(){
        val history = getTranslaionUseCase.execute()
        _translations.postValue(history)
    }

}
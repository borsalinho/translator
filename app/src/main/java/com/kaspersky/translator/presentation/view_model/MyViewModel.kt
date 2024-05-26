package com.kaspersky.translator.view_model


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.kaspersky.data.storage.dao.TranslationDao
import com.kaspersky.data.storage.database.AppDatabase
import com.kaspersky.data.storage.model.TranslationEntity
import com.kaspersky.domain.model.WordQuerry
import com.kaspersky.domain.model.WordsResponce
import com.kaspersky.domain.usecases.SaveWordToDBUseCase
import com.kaspersky.domain.usecases.SendWordUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MyViewModel(
    private var sendWordUseCase : SendWordUseCase,
    private var saveWordToDBUseCase : SaveWordToDBUseCase,
) : ViewModel() {




    private val _translations = MutableLiveData<List<TranslationEntity>>()
    val translations: LiveData<List<TranslationEntity>> get() = _translations

    private var _text_result = MutableLiveData<String>()
    val text_result: LiveData<String> get() = _text_result

    fun setTextResult(value: String) {
        _text_result.value = value
    }



    suspend fun sendWord(query : WordQuerry) : WordsResponce {
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



    private val _text_1 = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text_1: LiveData<String> = _text_1

    private val _text_2 = MutableLiveData<String>().apply {
        value = "This is favorite Fragment"
    }
    val text_2: LiveData<String> = _text_2
}
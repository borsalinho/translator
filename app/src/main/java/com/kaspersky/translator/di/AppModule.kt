package com.kaspersky.translator.di


import android.content.Context
import com.kaspersky.domain.model.WordTranslation
import com.kaspersky.domain.usecases.GetTranslaionUseCase
import com.kaspersky.domain.usecases.SaveWordToDBUseCase
import com.kaspersky.domain.usecases.SendWordUseCase
import com.kaspersky.domain.usecases.UpdateFavoriteUseCase
import com.kaspersky.translator.presentation.adapter.HistoryItemAdapter
import com.kaspersky.translator.view_model.MyViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val context: Context) {

    @Singleton
    @Provides
    fun providerContext() : Context{
        return context
    }

    @Singleton
    @Provides
    fun provideMyViewModel(
        sendWordUseCase : SendWordUseCase,
        saveWordToDBUseCase : SaveWordToDBUseCase,
        getTranslaionUseCase : GetTranslaionUseCase,
        updateFavoriteUseCase: UpdateFavoriteUseCase
    ): MyViewModel {
        return MyViewModel(
            sendWordUseCase = sendWordUseCase,
            saveWordToDBUseCase = saveWordToDBUseCase,
            getTranslaionUseCase = getTranslaionUseCase,
            updateFavoriteUseCase = updateFavoriteUseCase
        )
    }


    @Singleton
    @Provides
    fun provideHistoryItemAdapter(): HistoryItemAdapter {
        return HistoryItemAdapter(emptyList()){}
    }


}
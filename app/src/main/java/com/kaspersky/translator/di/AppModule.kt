package com.kaspersky.translator.di


import android.app.Application
import android.content.Context
import com.kaspersky.data.storage.database.AppDatabase
import com.kaspersky.domain.usecases.SaveWordToDBUseCase
import com.kaspersky.domain.usecases.SendWordUseCase
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
        saveWordToDBUseCase : SaveWordToDBUseCase
    ): MyViewModel {
        return MyViewModel(
            sendWordUseCase = sendWordUseCase,
            saveWordToDBUseCase = saveWordToDBUseCase
        )
    }



}
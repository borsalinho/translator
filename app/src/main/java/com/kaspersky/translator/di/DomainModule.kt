package com.kaspersky.translator.di

import com.kaspersky.domain.repository.TranslationRepositry
import com.kaspersky.domain.repository.WordQuerryRepository
import com.kaspersky.domain.usecases.GetFavoriteTranslationsUseCase
import com.kaspersky.domain.usecases.GetTranslaionUseCase
import com.kaspersky.domain.usecases.SaveWordToDBUseCase
import com.kaspersky.domain.usecases.SendWordUseCase
import com.kaspersky.domain.usecases.UpdateFavoriteUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DomainModule {

    @Singleton
    @Provides
    fun provideSendWordUseCase(wordQuerryRepository: WordQuerryRepository): SendWordUseCase {
        return SendWordUseCase(wordQuerryRepository = wordQuerryRepository)
    }

    @Singleton
    @Provides
    fun provideSaveWordToDBUseCase(wordQuerryRepository: WordQuerryRepository): SaveWordToDBUseCase {
        return SaveWordToDBUseCase(wordQuerryRepository = wordQuerryRepository)
    }

    @Singleton
    @Provides
    fun provideGetTranslaionUseCase(translationRepositry: TranslationRepositry): GetTranslaionUseCase {
        return GetTranslaionUseCase(translationRepositry = translationRepositry)
    }

    @Singleton
    @Provides
    fun provideUpdateFavoriteUseCase(translationRepositry: TranslationRepositry): UpdateFavoriteUseCase {
        return UpdateFavoriteUseCase(translationRepositry = translationRepositry)
    }

    @Singleton
    @Provides
    fun provideGetFavoriteTranslationsUseCase(translationRepositry: TranslationRepositry): GetFavoriteTranslationsUseCase {
        return GetFavoriteTranslationsUseCase(translationRepositry = translationRepositry)
    }
}


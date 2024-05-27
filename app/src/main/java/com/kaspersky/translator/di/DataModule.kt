package com.kaspersky.translator.di





import android.content.Context
import androidx.room.Room
import com.kaspersky.data.network.ApiSkyEnd
import com.kaspersky.data.repositoryimpl.TranslationRepositryImpl
import com.kaspersky.data.repositoryimpl.WordQuerryRepositryImpl
import com.kaspersky.data.storage.dao.TranslationDao
import com.kaspersky.data.storage.database.AppDatabase
import com.kaspersky.domain.repository.TranslationRepositry
import com.kaspersky.domain.repository.WordQuerryRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import javax.inject.Singleton

@Module
class DataModule {

    @Singleton
    @Provides
    fun provideWordQuerryRepositryImpl(
        apiSkyEng : ApiSkyEnd,
        translationDao : TranslationDao
    ) : WordQuerryRepository {
        return WordQuerryRepositryImpl(
            apiSkyEng = apiSkyEng,
            translationDao = translationDao
        )
    }

    @Singleton
    @Provides
    fun provideTranslationRepositry(
        translationDao : TranslationDao
    ) : TranslationRepositry{
        return TranslationRepositryImpl(
            translationDao = translationDao
        )
    }


    @Singleton
    @Provides
    fun provideRetrofit() : ApiSkyEnd {
        return Retrofit.Builder()
            .baseUrl("https://dictionary.skyeng.ru")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiSkyEnd::class.java)
    }


    @Singleton
    @Provides
    fun provideAppDatabase(сontext: Context): AppDatabase {
        return Room.databaseBuilder(
            сontext,
            AppDatabase::class.java,
            "translator-db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideTranslationDao(appDatabase: AppDatabase): TranslationDao {
        return appDatabase.translationDao()
    }


}


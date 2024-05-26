package com.kaspersky.translator.di


import android.app.Application
import com.kaspersky.translator.presentation.MainActivity
import com.kaspersky.translator.presentation.favorite.FavoriteFragment
import com.kaspersky.translator.presentation.home.HomeFragment
import com.kaspersky.translator.view_model.MyViewModel
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [
    AppModule::class,
    DomainModule::class,
    DataModule::class
])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(homeFragment: HomeFragment)
    fun inject(favoriteFragment: FavoriteFragment)


}
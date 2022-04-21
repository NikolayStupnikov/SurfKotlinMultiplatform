package ru.nikolay.stupnikov.surfkmp.android

import android.app.Application
import org.koin.android.ext.koin.androidContext
import ru.nikolay.stupnikov.interactor.di.initKoin

class AnimeApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@AnimeApp)
        }
    }
}
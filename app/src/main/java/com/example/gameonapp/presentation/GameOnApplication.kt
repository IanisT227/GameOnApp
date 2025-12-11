package com.example.gameonapp.presentation

import android.app.Application
import com.example.gameonapp.domain.dataStoreModule
import com.example.gameonapp.domain.databaseModule
import com.example.gameonapp.domain.repositoryModule
import com.example.gameonapp.domain.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GameOnApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@GameOnApplication)
            modules(listOf(databaseModule, repositoryModule, viewModelModule, dataStoreModule))
        }
    }
}
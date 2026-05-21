package com.example.gameonapp.domain

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.example.gameonapp.data.local.AppDatabase
import com.example.gameonapp.domain.repository.GameRepository
import com.example.gameonapp.domain.repository.SettingsRepository
import com.example.gameonapp.presentation.viewModels.FitnessViewModel
import com.example.gameonapp.presentation.viewModels.GameViewModel
import com.example.gameonapp.presentation.viewModels.SettingsViewModel
import com.example.gameonapp.utils.dataStore
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { FitnessViewModel(application = get()) }
    viewModel { GameViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
}

val repositoryModule = module {
    single { GameRepository(get()) }
    single { SettingsRepository(get()) }
}
val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "app_database"
        ).fallbackToDestructiveMigration(false).build()
    }

    single { get<AppDatabase>().gameDao() }
}

val dataStoreModule = module {
    single<DataStore<Preferences>> {
        androidContext().dataStore
    }
}
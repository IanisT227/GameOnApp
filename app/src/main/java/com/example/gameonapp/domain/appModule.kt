package com.example.gameonapp.domain

import com.example.gameonapp.presentation.viewModels.FitnessViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module{
    viewModel { FitnessViewModel(application = get()) }
}
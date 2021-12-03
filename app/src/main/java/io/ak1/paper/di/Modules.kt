package io.ak1.paper.di

import io.ak1.paper.ui.screens.home.HomeViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by akshay on 27/10/21
 * https://ak1.io
 */

/**
 * modules for dependency injection where [single] represents singleton class
 */
var databaseModule = module {
    single { getDb(androidApplication()) }
    single { getNoteTableDao(get()) }
    single { getFolderTableDao(get()) }
}

var viewModel = module {
    viewModel { HomeViewModel(get()) }
}

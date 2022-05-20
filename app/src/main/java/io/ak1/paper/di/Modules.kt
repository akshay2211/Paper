package io.ak1.paper.di

import io.ak1.paper.ui.screens.home.HomeViewModel
import io.ak1.paper.ui.screens.note.doodle.DoodleViewModel
import io.ak1.paper.ui.screens.note.note.NoteViewModel
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
    single { getDoodleTableDao(get()) }
    single { getImageTableDao(get()) }
    single { getFolderTableDao(get()) }
}

var viewModel = module {
    viewModel { HomeViewModel(get(), get()) }
    viewModel { NoteViewModel(get(), get()) }
    viewModel { DoodleViewModel(get(), get(), get()) }
}
var repositories = module {
    single { getLocalRepository() }
    single { getDoodleRepository(get()) }
    single { getNotesRepository(get(), get(), get()) }
}

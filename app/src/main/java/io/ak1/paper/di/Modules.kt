package io.ak1.paper.di

import io.ak1.paper.ui.screens.home.HomeViewModel
import io.ak1.paper.ui.screens.note.doodle.DoodleViewModel
import io.ak1.paper.ui.screens.note.image.ImageViewModel
import io.ak1.paper.ui.screens.note.note.NoteViewModel
import io.ak1.paper.ui.screens.note.options.OptionsViewModel
import io.ak1.paper.ui.screens.note.preview.PreviewViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Koin modules, split per feature so each one can later be lifted into its own
 * Gradle module without further refactoring.
 */

val databaseModule = module {
    single { getDb(androidApplication()) }
    single { getNoteTableDao(get()) }
    single { getDoodleTableDao(get()) }
    single { getImageTableDao(get()) }
    single { getFolderTableDao(get()) }
}

val repositoryModule = module {
    single { getLocalRepository() }
    single { getDoodleRepository(get()) }
    single { getImageRepository(get()) }
    single { getNotesRepository(get(), get(), get()) }
}

val homeModule = module {
    viewModelOf(::HomeViewModel)
}

val noteModule = module {
    viewModelOf(::NoteViewModel)
    viewModelOf(::OptionsViewModel)
    viewModelOf(::ImageViewModel)
    viewModelOf(::PreviewViewModel)
    viewModelOf(::DoodleViewModel)
}

val utilsModule = module {
    factory { getRandomNumber() }
}

val appModules = listOf(
    databaseModule,
    repositoryModule,
    homeModule,
    noteModule,
    utilsModule,
)

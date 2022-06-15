package io.ak1.paper.di

import android.content.Context
import androidx.room.Room
import io.ak1.paper.data.local.*
import io.ak1.paper.data.repositories.doodles.DoodlesRepository
import io.ak1.paper.data.repositories.doodles.impl.DoodlesRepositoryImpl
import io.ak1.paper.data.repositories.image.ImageRepository
import io.ak1.paper.data.repositories.image.impl.ImageRepositoryImpl
import io.ak1.paper.data.repositories.local.LocalRepository
import io.ak1.paper.data.repositories.local.impl.LocalRepositoryImpl
import io.ak1.paper.data.repositories.notes.NotesRepository
import io.ak1.paper.data.repositories.notes.impl.NotesRepositoryImpl
import kotlin.random.Random
import kotlin.random.nextInt

/**
 * Created by akshay on 27/10/21
 * https://ak1.io
 */

/**
 * definitions for dependency injection for all selected classes
 */

fun getDb(context: Context): AppDatabase {
    return synchronized(context) {
        Room.databaseBuilder(
            context,
            AppDatabase::class.java, "database-paper"
        ).fallbackToDestructiveMigration().addMigrations(MIGRATION_1_2, MIGRATION_2_3).build()
    }
}

fun getNoteTableDao(appDatabase: AppDatabase) = appDatabase.noteDao()
fun getDoodleTableDao(appDatabase: AppDatabase) = appDatabase.doodleDao()
fun getImageTableDao(appDatabase: AppDatabase) = appDatabase.imageDao()

fun getFolderTableDao(appDatabase: AppDatabase) = appDatabase.folderDao()

fun getNotesRepository(
    notesDao: NoteDao,
    doodleDao: DoodleDao,
    imageDao: ImageDao
): NotesRepository = NotesRepositoryImpl(notesDao, doodleDao, imageDao)

fun getDoodleRepository(doodleDao: DoodleDao): DoodlesRepository = DoodlesRepositoryImpl(doodleDao)
fun getImageRepository(imageDao: ImageDao): ImageRepository = ImageRepositoryImpl(imageDao)

fun getLocalRepository(): LocalRepository = LocalRepositoryImpl()


fun getRandomNumber() = Random(System.currentTimeMillis()).nextInt(0..17)

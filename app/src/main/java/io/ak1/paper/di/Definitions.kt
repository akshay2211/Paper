package io.ak1.paper.di

import android.content.Context
import androidx.room.Room
import io.ak1.paper.data.local.AppDatabase
import io.ak1.paper.data.local.DoodleDao
import io.ak1.paper.data.local.ImageDao
import io.ak1.paper.data.local.MIGRATION_1_2
import io.ak1.paper.data.local.MIGRATION_2_3
import io.ak1.paper.data.local.NoteDao
import io.ak1.paper.data.repositories.doodles.impl.DoodlesRepositoryImpl
import io.ak1.paper.data.repositories.image.impl.ImageRepositoryImpl
import io.ak1.paper.data.repositories.local.impl.LocalRepositoryImpl
import io.ak1.paper.data.repositories.notes.impl.NotesRepositoryImpl
import io.ak1.paper.domain.repository.DoodlesRepository
import io.ak1.paper.domain.repository.ImageRepository
import io.ak1.paper.domain.repository.LocalRepository
import io.ak1.paper.domain.repository.NotesRepository
import kotlin.random.Random
import kotlin.random.nextInt

fun getDb(context: Context): AppDatabase = synchronized(context) {
    Room.databaseBuilder(context, AppDatabase::class.java, "database-paper")
        .fallbackToDestructiveMigration()
        .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
        .build()
}

fun getNoteTableDao(appDatabase: AppDatabase) = appDatabase.noteDao()
fun getDoodleTableDao(appDatabase: AppDatabase) = appDatabase.doodleDao()
fun getImageTableDao(appDatabase: AppDatabase) = appDatabase.imageDao()
fun getFolderTableDao(appDatabase: AppDatabase) = appDatabase.folderDao()

fun getNotesRepository(
    notesDao: NoteDao,
    doodleDao: DoodleDao,
    imageDao: ImageDao,
): NotesRepository = NotesRepositoryImpl(notesDao, doodleDao, imageDao)

fun getDoodleRepository(doodleDao: DoodleDao): DoodlesRepository = DoodlesRepositoryImpl(doodleDao)
fun getImageRepository(imageDao: ImageDao): ImageRepository = ImageRepositoryImpl(imageDao)
fun getLocalRepository(): LocalRepository = LocalRepositoryImpl()

fun getRandomNumber() = Random(System.currentTimeMillis()).nextInt(0..17)

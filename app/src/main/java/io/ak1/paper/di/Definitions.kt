package io.ak1.paper.di

import android.content.Context
import androidx.room.Room
import io.ak1.paper.data.local.AppDatabase
import io.ak1.paper.data.local.MIGRATION_1_2
import io.ak1.paper.data.local.MIGRATION_2_3
import io.ak1.paper.data.local.NoteDao
import io.ak1.paper.data.repositories.local.LocalRepository
import io.ak1.paper.data.repositories.local.impl.LocalRepositoryImpl
import io.ak1.paper.data.repositories.notes.NotesRepository
import io.ak1.paper.data.repositories.notes.impl.NotesRepositoryImpl

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
        ).addMigrations(MIGRATION_1_2,MIGRATION_2_3).build()
    }
}

fun getNoteTableDao(appDatabase: AppDatabase) = appDatabase.noteDao()

fun getFolderTableDao(appDatabase: AppDatabase) = appDatabase.folderDao()

fun getNotesRepository(notesDao: NoteDao): NotesRepository = NotesRepositoryImpl(notesDao)

fun getLocalRepository(): LocalRepository = LocalRepositoryImpl()

package io.ak1.paper.di

import android.content.Context
import androidx.room.Room
import io.ak1.paper.data.local.AppDatabase

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
        ).build()
    }
}

fun getNoteTableDao(appDatabase: AppDatabase) = appDatabase.noteDao()

fun getFolderTableDao(appDatabase: AppDatabase) = appDatabase.folderDao()

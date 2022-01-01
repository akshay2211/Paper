package io.ak1.paper.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Created by akshay on 01/01/22
 * https://ak1.io
 */

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE notes_table ADD COLUMN doodle STRING;")
    }
}

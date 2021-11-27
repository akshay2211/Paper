package io.ak1.writedown.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import io.ak1.writedown.models.Folder
import io.ak1.writedown.models.Note

/**
 * Created by akshay on 27/10/21
 * https://ak1.io
 */

@Database(entities = [Note::class, Folder::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun folderDao(): FolderDao
}

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Query("SELECT * FROM notes_table WHERE id = :id")
    fun getNoteById(id: String): Note?

    @Query("SELECT * FROM notes_table ORDER BY updatedOn ASC")
    fun getAllNotes(): List<Note>

    @Query("SELECT * FROM notes_table WHERE folderId = :id ORDER BY updatedOn ASC")
    fun getAllNotesByFolderId(id: String): LiveData<List<Note>>

    @Query("DELETE FROM notes_table")
    suspend fun deleteTable()
}

@Dao
interface FolderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(folder: Folder)

    @Query("SELECT * FROM folder_table")
    fun getAllFolders(): List<Folder>

    @Query("DELETE FROM folder_table")
    suspend fun deleteTable()

}
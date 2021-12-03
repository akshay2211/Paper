package io.ak1.paper.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import io.ak1.paper.models.Folder
import io.ak1.paper.models.Note

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
    suspend fun getNoteById(id: String): Note?

    @Query("SELECT * FROM notes_table ORDER BY updatedOn DESC")
    fun getAllNotes(): List<Note>

    @Query("SELECT * FROM notes_table WHERE folderId = :id ORDER BY updatedOn DESC")
    fun getAllNotesByFolderId(id: String): LiveData<List<Note>>

    @Query("SELECT * FROM notes_table WHERE description LIKE '%' || :query || '%' ORDER BY updatedOn DESC")
    fun getNotesBySearch(query: String): LiveData<List<Note>>

    @Query("DELETE FROM notes_table WHERE id = :id")
    suspend fun deleteNote(id: String)

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
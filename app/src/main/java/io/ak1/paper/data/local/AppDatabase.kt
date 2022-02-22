package io.ak1.paper.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import io.ak1.paper.models.*

/**
 * Created by akshay on 27/10/21
 * https://ak1.io
 */

@Database(
    version = 4,
    entities = [Note::class, Doodle::class, Image::class, Folder::class],
    autoMigrations = [
        AutoMigration(from = 3, to = 4)
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun folderDao(): FolderDao
    abstract fun doodleDao(): DoodleDao
    abstract fun imageDao(): ImageDao
}

@Dao
interface DoodleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(doodle: Doodle)

    @Query("DELETE FROM doodle_table WHERE doodleid = :id")
    suspend fun deleteDoodle(id: String)

    @Query("DELETE FROM doodle_table WHERE attachedNoteId = :id")
    suspend fun deleteDoodleByNote(id: String)
}

@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(image: Image)

    @Query("DELETE FROM image_table WHERE imageId = :id")
    suspend fun deleteImage(id: String)

    @Query("DELETE FROM image_table WHERE attachedNoteId = :id")
    suspend fun deleteImageByNote(id: String)
}

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Query("SELECT * FROM notes_table WHERE noteId = :id")
    suspend fun getNoteById(id: String): Note?

    @Query("SELECT * FROM notes_table ORDER BY updatedOn DESC")
    fun getAllNotes(): List<Note>

    @Transaction
    @Query("SELECT * FROM notes_table WHERE folderId = :id ORDER BY updatedOn DESC")
    fun getAllNotesByFolderId(id: String): LiveData<List<NoteWithDoodleAndImage>>

    @Transaction
    @Query("SELECT * FROM notes_table WHERE description LIKE '%' || :query || '%' ORDER BY updatedOn DESC")
    fun getNotesBySearch(query: String): LiveData<List<NoteWithDoodleAndImage>>

    @Query("DELETE FROM notes_table WHERE noteId = :id")
    suspend fun deleteNote(id: String)

    @Query("DELETE FROM notes_table")
    suspend fun deleteTable()

    @Query("SELECT Count(*) FROM notes_table WHERE folderId = :folderId")
    suspend fun getNotesCountByFolderId(folderId: String): Int

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
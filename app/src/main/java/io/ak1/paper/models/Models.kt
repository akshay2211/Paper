package io.ak1.paper.models

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.*
import io.ak1.paper.ui.utils.convert
import kotlinx.parcelize.Parcelize
import java.util.*

/**
 * Created by akshay on 27/11/21
 * https://ak1.io
 */
@Parcelize
open class Parent : Parcelable {
    var createdOn: Long = System.currentTimeMillis()
    var updatedOn: Long = System.currentTimeMillis()
}

@Parcelize
@Entity(tableName = "notes_table", indices = [Index(value = ["noteId"], unique = true)])
data class Note(
    var folderId: String,
    var description: String
) : Parent() {
    @PrimaryKey
    var noteId: String = UUID.randomUUID().toString()
}

@Parcelize
@Entity(tableName = "doodle_table", indices = [Index(value = ["doodleid"], unique = true)])
data class Doodle(var attachedNoteId: String, var rawText: String, var base64Text: String,
                  @ColumnInfo(defaultValue = "") var uri:String) : Parent() {
    @PrimaryKey
    var doodleid: String = UUID.randomUUID().toString()
}

@Parcelize
@Entity(tableName = "image_table", indices = [Index(value = ["imageId"], unique = true)])
data class Image(var attachedNoteId: String, var imageText: String, var imageDesc: String?,
                 @ColumnInfo(defaultValue = "") var uri:String) :
    Parent() {
    @PrimaryKey
    var imageId: String = UUID.randomUUID().toString()
}

data class NoteWithDoodleAndImage(
    @Embedded val note: Note,
    @Relation(parentColumn = "noteId", entityColumn = "attachedNoteId")
    val imageList: List<Image>,
    @Relation(parentColumn = "noteId", entityColumn = "attachedNoteId")
    val doodleList: List<Doodle>
)

fun NoteWithDoodleAndImage.getBitmapList(): MutableList<Bitmap?> {
    return imageList.map {
        it.imageText.convert()
    }.toMutableList().apply {
        addAll(doodleList.map { it.base64Text.convert() })
    }
}
class ClickableUri(var id: String, var uri: String, var updatedOn: Long, var isDoodle: Boolean)

@Parcelize
@Entity(tableName = "folder_table", indices = [Index(value = ["id"], unique = true)])
data class Folder(
    var name: String
) : Parcelable {
    @PrimaryKey
    var id: String = UUID.randomUUID().toString()
}

package io.ak1.paper.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

/**
 * Created by akshay on 27/11/21
 * https://ak1.io
 */


@Parcelize
@Entity(tableName = "notes_table", indices = [Index(value = ["id"], unique = true)])
data class Note(
    var description: String,
    var createdOn: Long = System.currentTimeMillis(),
    var updatedOn: Long = System.currentTimeMillis(),
    val folderId: String
) : Parcelable {
    @PrimaryKey
    var id: String = UUID.randomUUID().toString()
}

@Parcelize
@Entity(tableName = "folder_table", indices = [Index(value = ["id"], unique = true)])
data class Folder(
    var name: String
) : Parcelable {
    @PrimaryKey
    var id: String = UUID.randomUUID().toString()
}
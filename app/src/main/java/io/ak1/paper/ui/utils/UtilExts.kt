package io.ak1.paper.ui.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.format.DateUtils
import android.util.Base64
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavType
import androidx.navigation.navArgument
import io.ak1.paper.R
import io.ak1.paper.models.ClickableUri
import io.ak1.paper.models.NoteWithDoodleAndImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by akshay on 30/11/21
 * https://ak1.io
 */

fun Long.timeAgo() = DateUtils.getRelativeTimeSpanString(
    this,
    Calendar.getInstance().timeInMillis,
    DateUtils.DAY_IN_MILLIS
).toString()

fun Long.timeAgoInSeconds() = DateUtils.getRelativeTimeSpanString(
    this,
    Calendar.getInstance().timeInMillis,
    DateUtils.SECOND_IN_MILLIS
).toString()


fun String.gridTrim(maxDigits: Int = 100) =
    if (this.length > maxDigits) "${this.substring(0, maxDigits)}..." else this


fun Bitmap.getEncodedString(): String? = Base64.encodeToString(ByteArrayOutputStream().apply {
    val w = 360
    val h = this@getEncodedString.height * w / this@getEncodedString.width
    Bitmap.createScaledBitmap(this@getEncodedString, w, h, false)
        .compress(Bitmap.CompressFormat.PNG, 99, this)
}.toByteArray(), Base64.NO_WRAP)


@Throws(IllegalArgumentException::class)
fun String.convert(): Bitmap? {
    val decodedByteArray: ByteArray = Base64.decode(this, Base64.NO_WRAP)
    return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.size)
}

fun String.withArg(arg: String, isDef: Boolean = false) =
    if (isDef) "$this/{$arg}" else "$this/$arg"

fun String.toArgs() = listOf(navArgument(this) {
    type = NavType.StringType
})

fun Modifier.paddingBottom(paddingValues: PaddingValues): Modifier {
    val pv = paddingValues.calculateBottomPadding()
    return padding(0.dp, 0.dp, 0.dp, if (pv > 46.dp) pv - 46.dp else pv)
}

fun Int.toPercent(dependency: Float) =
    this.let { it -> 1 - ((it * 100 / dependency) / 100f) }

fun Context.clickImage(currentPhotoPath: MutableState<String>, callback: (Uri) -> Unit) {

    val photoFile: File? = try {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
        val storageDir: File? = getExternalFilesDir("${Environment.DIRECTORY_PICTURES}/Paper")
        if (storageDir?.exists() == true)
            storageDir.mkdir()
        File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )
            .apply {
                // Save a file: path for use with ACTION_VIEW intents
                currentPhotoPath.value = absolutePath
            }
    } catch (ex: IOException) {
        // Error occurred while creating the File
        null
    }
    // Continue only if the File was successfully created
    photoFile?.also {
        val photoURI: Uri = FileProvider.getUriForFile(
            this,
            getString(R.string.paper_file_provider),
            it
        )
        callback.invoke(photoURI)
    }
}

//writing files to storage via scope and normal manner acc. to Api level
internal fun Context.saveImage(bitmap: Bitmap?, imageName: String): Uri? {
    var uri: Uri? = null
    try {
        val fileName = "$imageName.jpg"
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/Paper")
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            } else {
                val directory =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val file = File("$directory/Paper", fileName)
                if (file.exists())
                    file.mkdir()
                put(MediaStore.MediaColumns.DATA, file.absolutePath)
            }
        }

        uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        uri?.let {
            contentResolver.openOutputStream(it).use { output ->
                bitmap?.compress(Bitmap.CompressFormat.PNG, 0, output)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                values.apply {
                    clear()
                    put(MediaStore.Audio.Media.IS_PENDING, 0)
                }
                contentResolver.update(uri, values, null, null)
            }
        }
        return uri
    } catch (e: java.lang.Exception) {
        if (uri != null) {
            // Don't leave an orphan entry in the MediaStore
            contentResolver.delete(uri, null, null)
        }
        throw e
    }
}

fun NoteWithDoodleAndImage.getUriList(): MutableList<ClickableUri> {
    val list = doodleList.map {
        ClickableUri(it.doodleid, it.uri, it.updatedOn, true)
    }.toMutableList()
    val list2 = imageList.map { ClickableUri(it.imageId, it.uri, it.updatedOn, false) }
    list.addAll(list2)
    list.sortBy { it.updatedOn }
    list.reverse()
    return list
}


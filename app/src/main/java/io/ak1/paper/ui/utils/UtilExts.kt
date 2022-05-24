package io.ak1.paper.ui.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
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
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
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
            "io.ak1.paper.fileprovider",
            it
        )
        callback.invoke(photoURI)
    }
}


package io.ak1.paper.ui.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.format.DateUtils
import android.util.Base64
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.io.ByteArrayOutputStream
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
    val w = 240
    val h = this@getEncodedString.height * w / this@getEncodedString.width
    Bitmap.createScaledBitmap(this@getEncodedString, w, h, false)
        .compress(Bitmap.CompressFormat.PNG, 70, this)
}.toByteArray(), Base64.NO_WRAP)


@Throws(IllegalArgumentException::class)
fun String.convert(): Bitmap? {
    val decodedByteArray: ByteArray = Base64.decode(this, Base64.NO_WRAP)
    return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.size)
}

fun Modifier.limitWidthInWideScreen(width: Dp = 640.dp) = this
    .fillMaxWidth()
    .widthIn(max = width)
    .wrapContentWidth(align = Alignment.CenterHorizontally)


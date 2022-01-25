package io.ak1.paper.ui.utils

import android.graphics.Bitmap
import android.text.format.DateUtils
import android.util.Base64
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
            this@getEncodedString.compress(Bitmap.CompressFormat.PNG, 100, this)
        }.toByteArray(), Base64.DEFAULT)
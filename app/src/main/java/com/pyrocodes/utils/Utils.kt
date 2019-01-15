package com.pyrocodes.utils

import java.text.DateFormatSymbols
import java.text.SimpleDateFormat

class Utils {
    companion object {
        fun getMonthForInt(num: Int): String {
            return if (num in 0..11) {
                DateFormatSymbols().months[num]
            } else {
                "wrong"
            }

        }

        fun getTimeFormated(format: String, TimeInMilies: Long): String {
            return SimpleDateFormat(format).format(TimeInMilies)
        }
    }
}
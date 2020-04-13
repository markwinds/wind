package com.bangtong.wind.util

import android.icu.text.SimpleDateFormat
import java.util.*

class TimeControl {

    companion object{
        private const val format = "yyyy.MM.dd HH:mm"
        fun convertLongToTime(time: Long): String {
            val date = Date(time)
            val format = SimpleDateFormat(format)
            return format.format(date)
        }

        fun getTimeToLong(): Long {
            return System.currentTimeMillis()
        }

        fun convertDateToLong(date: String): Long {
            val df = SimpleDateFormat(format)
            return df.parse(date).time
        }
    }

}
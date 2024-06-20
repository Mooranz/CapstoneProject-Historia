package com.tugas.capstoneproject_historia.utils

import android.util.Log
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateFormatter {
    fun formatDate(currentDate: String): String? {
        val currentFormat = "yyyy-MM-dd'T'hh:mm:ss'Z'"
        val targetFormat = "dd MMM yyyy | HH:mm"
        val timezone = "GMT"
        val currentDf: DateFormat = SimpleDateFormat(currentFormat, Locale.getDefault())
        currentDf.timeZone = TimeZone.getTimeZone(timezone)
        val targetDf: DateFormat = SimpleDateFormat(targetFormat, Locale.getDefault())
        var targetDate: String? = null
        try {
            val date = currentDf.parse(currentDate)
            if (date != null) {
                targetDate = targetDf.format(date)
            }
        } catch (ex: ParseException) {
            ex.printStackTrace()
        }
        return targetDate
    }

    fun formatLongToDate(millis: Long): String {
        val targetFormat = "dd MMMM yyyy | HH:mm"
        val targetDF: DateFormat = SimpleDateFormat(targetFormat, Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        Log.d("DateFormat", "${targetDF.format(calendar.time)}")
        return targetDF.format(calendar.time)
    }

    fun formatDateToDate(currentDate: String): String? {
        val currentFormat = "dd MMMM yyyy | HH:mm"
        val targetFormat = "dd MMMM yyyy"
        val currentDf: DateFormat = SimpleDateFormat(currentFormat, Locale.getDefault())
        val targetDf: DateFormat = SimpleDateFormat(targetFormat, Locale.getDefault())
        var targetDate: String? = null
        try {
            val date = currentDf.parse(currentDate)
            if (date != null) {
                targetDate = targetDf.format(date)
            }
        } catch (ex: ParseException) {
            ex.printStackTrace()
        }
        return targetDate
    }

    fun formatDateToHours(currentDate: String): String? {
        val currentFormat = "dd MMMM yyyy | HH:mm"
        val targetFormat = "HH:mm"
        val currentDf: DateFormat = SimpleDateFormat(currentFormat, Locale.getDefault())
        val targetDf: DateFormat = SimpleDateFormat(targetFormat, Locale.getDefault())
        var targetDate: String? = null
        try {
            val date = currentDf.parse(currentDate)
            if (date != null) {
                targetDate = targetDf.format(date)
            }
        } catch (ex: ParseException) {
            ex.printStackTrace()
        }
        return targetDate
    }
}

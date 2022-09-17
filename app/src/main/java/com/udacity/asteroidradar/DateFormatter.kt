package com.udacity.asteroidradar

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class DateFormatter {
    private val simpleDateFormat = SimpleDateFormat(
        Constants.API_QUERY_DATE_FORMAT,
        Locale.getDefault()
    )

    fun parseDate(date: Date, format: String): String? {
        var stringDate: String? = null
        try {
            stringDate = simpleDateFormat.format(Date(System.currentTimeMillis())).toString()
        } catch (e: Exception) {
            Log.d("DateFormatter", e.toString())
        }
        return stringDate
    }
}
package com.android.locationtracker.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TimeFormatter {
    private val sdf = SimpleDateFormat("d MMM yyyy, HH.mm", Locale.ENGLISH)

    fun format(millis: Long): String = sdf.format(Date(millis))
}
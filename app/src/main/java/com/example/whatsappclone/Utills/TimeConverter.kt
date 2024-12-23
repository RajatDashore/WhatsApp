package com.example.whatsappclone.Utills

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.concurrent.TimeUnit

class TimeConverter {

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    fun timeFormat(time: Long): String {
        val currentTimeMs = System.currentTimeMillis()
        val differanceTime = currentTimeMs - time

        val second: Long = TimeUnit.MILLISECONDS.toSeconds(differanceTime)
        val minute: Long = TimeUnit.MILLISECONDS.toMinutes(differanceTime)
        val hour: Long = TimeUnit.MILLISECONDS.toHours(differanceTime)
        val days: Long = TimeUnit.MILLISECONDS.toDays(differanceTime)
        val month: Long = days / 30
        val year: Long = days / 365

        try {
            return when {
                second < 60 -> {
                    val dateFormat = SimpleDateFormat("h:mm a")
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = time
                    dateFormat.format(calendar.time)
                }

                minute < 60 -> {
                    val dateFormat = SimpleDateFormat("h:mm a")
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = time
                    dateFormat.format(calendar.time)
                }

                hour < 24 -> {
                    val dateFormat = SimpleDateFormat("h:mm a")
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = time
                    dateFormat.format(calendar.time)
                }

                days == 1L -> "yesterday"
                days < 7 -> {
                    val dateFormat = SimpleDateFormat("EEE")
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = time
                    dateFormat.format(calendar.time)
                }

                days in 7..13 -> {

                    "last week"

                }

                days < 30 -> {
                    val dateFormat = SimpleDateFormat("dd MMM")
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = time
                    dateFormat.format(calendar.time)
                }

                days < 365 -> {
                    val dateFormat = SimpleDateFormat("dd MMM yyyy")
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = time
                    dateFormat.format(calendar.time)
                }

                else -> if (year.toInt() == 1) "1 year ago" else "$year years ago"
            }
        } catch (e: Exception) {
            return ""
        }

        return ""
    }
}

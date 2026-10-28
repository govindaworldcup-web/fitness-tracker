package com.example.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {
    private const val ISO_PATTERN = "yyyy-MM-dd"
    private val isoFormat = SimpleDateFormat(ISO_PATTERN, Locale.US)
    private val displayFormat = SimpleDateFormat("EEE, MMM d", Locale.US)
    private val fullDisplayFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.US)
    private val shortDayFormat = SimpleDateFormat("EEE", Locale.US)

    fun getTodayIso(): String {
        return isoFormat.format(Date())
    }

    fun formatDisplayDate(isoDate: String): String {
        return try {
            val date = isoFormat.parse(isoDate) ?: return isoDate
            val todayIso = getTodayIso()
            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_YEAR, -1)
            val yesterdayIso = isoFormat.format(cal.time)

            when (isoDate) {
                todayIso -> "Today"
                yesterdayIso -> "Yesterday"
                else -> displayFormat.format(date)
            }
        } catch (_: Exception) {
            isoDate
        }
    }

    fun formatFullDate(isoDate: String): String {
        return try {
            val date = isoFormat.parse(isoDate) ?: return isoDate
            fullDisplayFormat.format(date)
        } catch (_: Exception) {
            isoDate
        }
    }

    fun getShortDayLabel(isoDate: String): String {
        return try {
            val date = isoFormat.parse(isoDate) ?: return ""
            shortDayFormat.format(date)
        } catch (_: Exception) {
            ""
        }
    }

    fun addDaysToIso(isoDate: String, daysToAdd: Int): String {
        return try {
            val date = isoFormat.parse(isoDate) ?: Date()
            val cal = Calendar.getInstance()
            cal.time = date
            cal.add(Calendar.DAY_OF_YEAR, daysToAdd)
            isoFormat.format(cal.time)
        } catch (_: Exception) {
            getTodayIso()
        }
    }

    fun getLast7DaysIso(): List<String> {
        val list = mutableListOf<String>()
        val cal = Calendar.getInstance()
        // End at today, start 6 days ago
        for (i in 6 downTo 0) {
            val c = Calendar.getInstance()
            c.add(Calendar.DAY_OF_YEAR, -i)
            list.add(isoFormat.format(c.time))
        }
        return list
    }
}

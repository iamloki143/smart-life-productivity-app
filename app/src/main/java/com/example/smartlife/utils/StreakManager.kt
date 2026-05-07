package com.example.smartlife.utils

import android.content.Context
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StreakManager(context: Context) {

    private val prefs =
        context.getSharedPreferences(
            "streak_prefs",
            Context.MODE_PRIVATE
        )

    companion object {
        private const val STREAK_COUNT = "streak_count"
        private const val LAST_COMPLETED_DATE = "last_completed_date"
    }

    fun getCurrentStreak(): Int {
        return prefs.getInt(STREAK_COUNT, 0)
    }

    fun updateStreak() {

        val today =
            SimpleDateFormat(
                "yyyyMMdd",
                Locale.getDefault()
            ).format(Date())

        val lastDate =
            prefs.getString(LAST_COMPLETED_DATE, null)

        var streak =
            prefs.getInt(STREAK_COUNT, 0)

        if (lastDate == null) {

            streak = 1

        } else {

            val diff =
                today.toInt() - lastDate.toInt()

            when (diff) {

                0 -> return

                1 -> streak++

                else -> streak = 1
            }
        }

        prefs.edit()
            .putInt(STREAK_COUNT, streak)
            .putString(LAST_COMPLETED_DATE, today)
            .apply()
    }
}
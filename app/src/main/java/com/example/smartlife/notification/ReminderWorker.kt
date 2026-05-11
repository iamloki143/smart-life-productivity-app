package com.example.smartlife.notification

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class ReminderWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {

        NotificationHelper.showNotification(
            applicationContext,
            "SmartLife 🚀",
            "Stay focused and complete your tasks today."
        )

        return Result.success()
    }
}
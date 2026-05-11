package com.example.smartlife.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {

        val title =
            intent.getStringExtra("title")
                ?: "SmartLife"

        val message =
            intent.getStringExtra("message")
                ?: "Task Reminder"

        NotificationHelper.showNotification(
            context,
            title,
            message
        )
        Log.d("ALARM", "Notification Triggered")
    }
}
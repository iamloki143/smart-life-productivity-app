package com.example.smartlife

import android.Manifest
import android.app.AlarmManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.smartlife.navigation.BottomBar
import com.example.smartlife.navigation.NavGraph
import com.example.smartlife.notification.ReminderWorker
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    private val requestNotificationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                startActivity(intent)
            }
        }

        setContent {
            val workRequest = PeriodicWorkRequestBuilder<ReminderWorker>(
                24, TimeUnit.HOURS
            ).build()

            WorkManager.getInstance(this)
                .enqueueUniquePeriodicWork(
                    "daily_reminder",
                    ExistingPeriodicWorkPolicy.UPDATE,
                    workRequest
                )

            val navController = rememberNavController()
            Scaffold(
                bottomBar = {
                    BottomBar(navController)
                }
            ) { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    NavGraph(navController)
                }
            }
        }
    }
}
package com.itgates.co.pulpo.ultra.onlineLogService

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.SystemClock
import com.itgates.co.pulpo.ultra.viewModels.OnlineLogViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnlineDebuggingService: Service() {

    @Inject
    lateinit var onlineLogViewModel: OnlineLogViewModel

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Send Online Log
        if (OnlineDebuggingManager.isEnabled) {
            val uploadedList = OnlineDebuggingManager.getUploadedList()
            if (uploadedList.logs.isNotEmpty()) onlineLogViewModel.uploadOnlineDebuggingData(uploadedList)

            // Schedule the next execution after 5 minutes
            scheduleNextExecution()
        }

        // Return START_STICKY to ensure the service restarts if it gets killed by the system
        return START_STICKY
    }

    private fun scheduleNextExecution() {
        // Use AlarmManager to schedule the next execution after 5 minutes
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, OnlineDebuggingService::class.java)
        val pendingIntent = PendingIntent.getService(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        // Set the alarm to trigger after 1 minutes (adjust as needed)
//        val intervalMillis = 1 * 60 * 1000L // 1 minutes in milliseconds
        val intervalMillis = 7 * 1000L // 1 minutes in milliseconds
        val triggerTime = SystemClock.elapsedRealtime() + intervalMillis

        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pendingIntent)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
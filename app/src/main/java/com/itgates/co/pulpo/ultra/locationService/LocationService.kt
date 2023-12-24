package com.itgates.co.pulpo.ultra.locationService

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.SystemClock
import com.itgates.co.pulpo.ultra.utilities.Utilities
import com.itgates.co.pulpo.ultra.viewModels.LocationViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LocationService: Service() {

    @Inject
    lateinit var locationViewModel: LocationViewModel

    var num = 0

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Your code to perform the desired function goes here
        performYourFunction()

        // Schedule the next execution after 5 minutes
        scheduleNextExecution()

        // Return START_STICKY to ensure the service restarts if it gets killed by the system
        return START_STICKY
    }

    private fun performYourFunction() {
        // Implement the logic for your function here
//        Utilities.createCustomToast(this, "doo ${num++}")
        locationViewModel.saveOfflineLoc(Utilities.createOfflineLoc(applicationContext))

        // Send Online Log

    }

    private fun scheduleNextExecution() {
        // Use AlarmManager to schedule the next execution after 5 minutes
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, LocationService::class.java)
        val pendingIntent = PendingIntent.getService(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        // Set the alarm to trigger after 5 minutes (adjust as needed)
//        val intervalMillis = 5 * 60 * 1000L // 5 minutes in milliseconds
        val intervalMillis = 1 * 7 * 1000L // 10 seconds in milliseconds
        val triggerTime = SystemClock.elapsedRealtime() + intervalMillis

        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pendingIntent)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
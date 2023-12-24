package com.itgates.co.pulpo.ultra.firebase

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.itgates.co.pulpo.ultra.R
import com.itgates.co.pulpo.ultra.ui.activities.MainActivity

const val channelId = "notification_channel"
const val channelName = "COM.IT-GATES.CO.PULPO.ULTRA"

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
//        super.onMessageReceived(message)
        println("--------------------------------------------------- 777 ----")
        println(message.notification!!.title)
        println("--------------------------------------------------- 777 ----")
        println(message.notification!!.body)
        println("--------------------------------------------------- 777 ----")
        println(message.notification!!.channelId)
        println("--------------------------------------------------- 777 ----")
        println(message.notification!!.color)
        println("--------------------------------------------------- 777 ----")
        println(message.notification!!.clickAction)
        println("--------------------------------------------------- 777 ----")
        println(message.notification!!.icon)
        println("--------------------------------------------------- 777 ----")
        println(message.messageType)
        println("--------------------------------------------------- 777 ----")
        println(message.from)
        println("--------------------------------------------------- 777 ----")
        println(message.data)
        println("--------------------------------------------------- 777 ----")
        if (message.notification != null) {
            message.notification!!.title?.let { title ->
                message.notification!!.body?.let { body ->
                    generateNotification(title, body)
                }
            }
        }
    }

    private fun getRemoteView(title: String, message: String): RemoteViews {
        val remoteView = RemoteViews("com.itgates.co.pulpo.ultra", R.layout.custom_notification)

        remoteView.setTextViewText(R.id.title, title)
        remoteView.setTextViewText(R.id.message, message)
        remoteView.setImageViewResource(R.id.iconView, R.drawable.polpo7)

        return remoteView
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun generateNotification(title: String, message: String) {
        val intent = Intent(this@MyFirebaseMessagingService, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this@MyFirebaseMessagingService,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        var builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.location_error)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(title, message))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(notificationChannel)
        notificationManager.notify(0, builder.build())
    }


}
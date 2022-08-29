package com.witnovus.nepzep.com.witnovus.freshbundeliveryapp.firebaseservice


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

import com.google.firebase.messaging.RemoteMessage
import com.witnovus.nepzep.utils.Event
import com.witnovus.freshbundeliveryapp.R
import com.witnovus.freshbundeliveryapp.ui.activity.AddAddressActivity
import com.witnovus.freshbundeliveryapp.ui.activity.DocumentUploadActivity
import com.witnovus.freshbundeliveryapp.ui.activity.MainActivity
import com.witnovus.freshbundeliveryapp.ui.base.BaseActivity
import com.witnovus.freshbundeliveryapp.utils.Constants


private var NOTIFY_ID = 0

/**
 * send Notifications.
 *
 */

fun NotificationManager.sendNotification(
    messageBody: RemoteMessage,
    applicationContext: Context
) {
   // TODO: Step 1.11 create intent
    var intentNotif: Intent? = null
    // Product update navigation

    if (messageBody.data[Constants.NOTIFICATION_ORDER_ID] == Constants.NOTIFICATION_ORDER_ID) {
        intentNotif = Intent(applicationContext, MainActivity::class.java)
        intentNotif.putExtra(
            Constants.NOTIFICATION_ORDER_ID,
            Constants.NOTIFICATION_ORDER_ID
        )
        intentNotif.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        (MainActivity).notificationHandledLivaData.postValue(Event(true))
    }else if (messageBody.data[Constants.NOTIFICATION_TITLE].equals(Constants.NOTIFICATION_PROFILE_FIRST_REVIEW)){
        intentNotif = Intent(applicationContext, DocumentUploadActivity::class.java)
        intentNotif.putExtra(
            Constants.NOTIFICATION_TITLE,
           Constants.NOTIFICATION_PROFILE_FIRST_REVIEW
        )
        intentNotif.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }else if (messageBody.data[Constants.NOTIFICATION_TITLE].equals(Constants.NOTIFICATION_DOCUMENT_VERIFIED)){
        intentNotif = Intent(applicationContext, AddAddressActivity::class.java)
        intentNotif.putExtra(
            Constants.NOTIFICATION_TITLE, Constants.NOTIFICATION_DOCUMENT_VERIFIED
        )
        intentNotif.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }else if (messageBody.data[Constants.NOTIFICATION_TITLE].equals(Constants.NOTIFICATION_PROFILE_APPROVED)){
        intentNotif = Intent(applicationContext, MainActivity::class.java)
        intentNotif.putExtra(
            Constants.NOTIFICATION_TITLE, Constants.NOTIFICATION_PROFILE_APPROVED
        )
        intentNotif.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }else {
        intentNotif = Intent(applicationContext, MainActivity::class.java)
        intentNotif.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    val pendingIntent = PendingIntent.getActivity(
        applicationContext,
        0,
        intentNotif,
        PendingIntent.FLAG_IMMUTABLE
    )

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val importance = NotificationManager.IMPORTANCE_HIGH

        val builder =
            NotificationCompat.Builder(applicationContext, Constants.NOTIFICATION_CHANNEL_ID)

        builder.setContentTitle(messageBody.data[Constants.NOTIFICATION_TITLE]) // required
            .setSmallIcon(R.drawable.ic_logo) // required
            .setContentText(messageBody.data[Constants.NOTIFICATION_BODY]) // required
            .setDefaults(Notification.DEFAULT_ALL)
            .setColor(applicationContext.getColor(R.color.menu_active))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setTicker(applicationContext.getString(R.string.app_name))
            .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)).priority =
            Notification.PRIORITY_HIGH

        NOTIFY_ID += 1
        val notification = builder.build()
        notify(NOTIFY_ID, notification)
    }
}

// TODO: Step 1.14 Cancel all notification
 /*Cancels all notifications.*/

fun NotificationManager.cancelNotifications() {
    cancelAll()
}


 /*send create Notifications Channels.*/


fun NotificationManager.createNotificationsChannels() {
    val importance = NotificationManager.IMPORTANCE_HIGH
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        var mChannel = getNotificationChannel(Constants.NOTIFICATION_CHANNEL_ID)
        if (mChannel == null) {
            mChannel =
                NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID, "NepZep-Delivery", importance)
            mChannel.enableVibration(true)
            mChannel.vibrationPattern =
                longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            createNotificationChannel(mChannel)
        }
    }
}

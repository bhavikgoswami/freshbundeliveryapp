package com.witnovus.freshbundeliveryapp.firebaseservice

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.witnovus.nepzep.com.witnovus.freshbundeliveryapp.firebaseservice.createNotificationsChannels
import com.witnovus.nepzep.com.witnovus.freshbundeliveryapp.firebaseservice.sendNotification


@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class NotificationService :
    FirebaseMessagingService() {
    private var manager: NotificationManager? = null
    private var broadcaster: LocalBroadcastManager? = null
    private var NOTIFY_ID = 0
    private var context: Context? = null

    override fun onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this)
    }

    override fun onNewToken(token: String) {

        super.onNewToken(token)
    }
    override fun handleIntent(intent: Intent?) {
        super.handleIntent(intent)
        Log.e("message intent", intent.toString())
    }


    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.e("message message", message.toString())
        if (message.data.isNotEmpty()) {
            Log.d("message message", "Message data payload: ${message.data}")
            val builder: NotificationCompat.Builder

            val manager = ContextCompat.getSystemService(applicationContext, NotificationManager::class.java) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                manager.createNotificationsChannels()
                manager.sendNotification(message,this)
            }
        }
    }
}
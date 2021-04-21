package com.estazo.project.seeable.app

import android.content.Context
import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class NotificationService : NotificationListenerService() {
    var context: Context? = null
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        Log.i("kuyjaa"," call create class noti")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val pack = sbn.packageName
//        val ticker = sbn.notification.tickerText.toString()
        val extras = sbn.notification.extras
        val title = extras.getString("android.title").toString()
        val text = extras.getCharSequence("android.text").toString()
        if (title != "null" && text != "null" ){
            Log.i("Package", pack)
//        Log.i("Ticker", ticker)
            Log.i("Title", title)
            Log.i("Text", text)
            val msgrcv = Intent("Msg")
            msgrcv.putExtra("package", pack)
//        msgrcv.putExtra("ticker", ticker)
            msgrcv.putExtra("title", title)
            msgrcv.putExtra("text", text)
            LocalBroadcastManager.getInstance(context!!).sendBroadcast(msgrcv)
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        Log.i("Msg", "Notification Removed")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("kuyjaa"," call destroy")
    }

}
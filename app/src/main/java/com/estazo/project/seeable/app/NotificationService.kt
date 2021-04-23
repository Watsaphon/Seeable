package com.estazo.project.seeable.app

import android.content.Context
import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import java.util.regex.Pattern

class NotificationService : NotificationListenerService() {
    var context: Context? = null

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        Log.i("notificationService"," call create class NotificationService")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val pack = sbn.packageName
        val extras = sbn.notification.extras
        val title = extras.getString("android.title").toString()
        val text = extras.getCharSequence("android.text").toString()

        val regex = "ETA".toRegex()
        val matchResult = regex.find(text)?.value.toString()

        Log.i("notificationService"," pack : $pack , title : $title , text : $text , regEX -> matchResult : $matchResult ")

        if (pack == "com.google.android.apps.maps" && title != "null" && text != "null" && matchResult == "ETA" ){
            Log.i("Package", pack)
            Log.i("Title", title)
            Log.i("Text", text)
            val msgrcv = Intent("Msg")
            msgrcv.putExtra("package", pack)
            msgrcv.putExtra("title", title)
            msgrcv.putExtra("text", text)
            LocalBroadcastManager.getInstance(context!!).sendBroadcast(msgrcv)
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        Log.i("Msg", "Notification Removed")
        val launchIntent = packageManager.getLaunchIntentForPackage("com.estazo.project.seeable.app")
        startActivity(launchIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("notificationService"," call destroy")
    }

}
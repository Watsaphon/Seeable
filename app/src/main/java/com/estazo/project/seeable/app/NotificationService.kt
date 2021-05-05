package com.estazo.project.seeable.app

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.android.synthetic.main.alert_dialog_critical_event.view.*
import kotlinx.android.synthetic.main.alert_dialog_go_to_app.view.*


class NotificationService : NotificationListenerService() {
    private lateinit var context: Context

    @RequiresApi(Build.VERSION_CODES.O)
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

        val regexNavigation = "navigation".toRegex()
        val checkNavigation = regexNavigation.find(title)?.value.toString()

        Log.i("notificationService"," pack : $pack , title : $title" +
                " , text : $text , regEX -> matchResult : $matchResult , checkNavigation : $checkNavigation ")

        if ( title != "null" && text != "null"){
            Log.i("Package", pack)
            Log.i("Title", title)
            Log.i("Text", text)
            val msgrcv = Intent("Msg")
            msgrcv.putExtra("package", pack)
            msgrcv.putExtra("title", title)
            msgrcv.putExtra("text", text)
            LocalBroadcastManager.getInstance(context!!).sendBroadcast(msgrcv)
            if(pack == "com.estazo.project.seeable.app" && title == "critical_Condition"){
                dialog()
            }
            if(pack == "com.estazo.project.seeable.app" && checkNavigation == "navigation"){
                Log.i("notificationService", "OK JA")
                alertDialogBack()
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        Log.i("notificationService", "Notification Removed")
        val launchIntent = packageManager.getLaunchIntentForPackage("com.estazo.project.seeable.app")
        startActivity(launchIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("notificationService"," call destroy")
    }

    private fun dialog() {

        val dialogBuilder = AlertDialog.Builder(this)
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.alert_dialog_critical_event, null)

        dialogBuilder.setView(mDialogView)

        Log.i("dialog","dialog call")
        val dialog: AlertDialog = dialogBuilder.create()
        val dialogWindow: Window = dialog.window!!
        val dialogWindowAttributes: WindowManager.LayoutParams = dialogWindow.attributes

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogWindowAttributes)
        lp.width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 280f, resources.displayMetrics).toInt()
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialogWindow.attributes = lp

        dialogWindow.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

        mDialogView.callAmbulance.setOnClickListener{
            Log.i("dialog","click call Ambulance ja")
            dialog.dismiss()
        }

        mDialogView.getLocation.setOnClickListener{
            Log.i("dialog","click get Location ja")
            dialog.dismiss()
        }

    }

    private fun alertDialogBack() {

        val dialogBuilder = AlertDialog.Builder(this)
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.alert_dialog_go_to_app, null)

        dialogBuilder.setView(mDialogView)

        Log.i("dialog","dialog call")
        val dialog: AlertDialog = dialogBuilder.create()
        val dialogWindow: Window = dialog.window!!
        val dialogWindowAttributes: WindowManager.LayoutParams = dialogWindow.attributes

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogWindowAttributes)
        lp.width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 280f, resources.displayMetrics).toInt()
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialogWindow.attributes = lp

        dialogWindow.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()


        mDialogView.gotoAppEvent.setOnClickListener{
            Log.i("alertDialogBack","click layout ja")
            dialog.dismiss()
            val launchIntent = packageManager.getLaunchIntentForPackage("com.estazo.project.seeable.app")
            startActivity(launchIntent)
        }

    }

}
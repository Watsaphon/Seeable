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
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.android.synthetic.main.alert_dialog_critical_event.view.*


class NotificationService : NotificationListenerService() {
    var context: Context? = null

    private var windowManager: WindowManager? = null
    private var chatHead: ImageView? = null

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


        Log.i("notificationService"," pack : $pack , title : $title" +
                " , text : $text , regEX -> matchResult : $matchResult ")

//        if (pack == "com.google.android.apps.maps" && title != "null" && text != "null" && matchResult == "ETA" ){
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

    private fun dialog() {
        val dialogBuilder = AlertDialog.Builder(this)
//        dialogBuilder.setTitle("Test Kub")
//        dialogBuilder.setMessage("Hello world")
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.alert_dialog_critical_event, null)
        //AlertDialogBuilder
//        val mBuilder = AlertDialog.Builder(activity).setView(mDialogView)

        dialogBuilder.setView(mDialogView)

//        mAlertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

//        dialogBuilder.setNegativeButton(R.string.btn_back,
//            DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() }
//        )



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
//        dialogWindowAttributes.windowAnimations = R.style.DialogAnimation
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

}
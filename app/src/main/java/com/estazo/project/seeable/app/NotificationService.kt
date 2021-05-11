package com.estazo.project.seeable.app

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.speech.tts.TextToSpeech
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.alert_dialog_critical_event.view.*
import kotlinx.android.synthetic.main.alert_dialog_go_to_app.view.*
import java.util.*


class NotificationService : NotificationListenerService() {

    private lateinit var context: Context
    private lateinit var sharedPrefNavigate : SharedPreferences

    var textToSpeech: TextToSpeech? = null
    private var phone: String = ""
    private lateinit var database: DatabaseReference
    private lateinit var listener: ValueEventListener

    private lateinit var language : String
    private lateinit var sharedPrefLanguage: SharedPreferences

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

        val regexCritical = "Critical".toRegex()
        val checkCritical = regexCritical.find(title)?.value.toString()

        Log.d("notificationServiceInfo"," pack : $pack , title : $title" +
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
            if(pack == "com.estazo.project.seeable.app" && checkCritical == "Critical"){

                val info = text.substring(5)
                phone = info

                val username = title.split(":".toRegex()).toTypedArray()
                val name  = username[0]
                Log.i("notificationServiceInfo","username :$username , name : $name")
                dialog(name)

            }
            if(pack == "com.estazo.project.seeable.app" && checkNavigation == "navigation"){
                Log.d("notificationServiceInfo", "OK JA")
                alertDialogBack()
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        Log.i("notificationService", "Notification Removed")
//        val launchIntent = packageManager.getLaunchIntentForPackage("com.estazo.project.seeable.app")
//        startActivity(launchIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("notificationService"," call destroy")
    }

    private fun dialog(name: String) {

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
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()

        sharedPrefLanguage = getSharedPreferences("value", 0)
        language = sharedPrefLanguage.getString("stringKey", "not found!").toString()

        when(language){
            "en"->{mDialogView.titleInfo.text = ": has been fell"
                mDialogView.callAmbulance.text = "Call Ambulance"
                mDialogView.getLocation.text = "Get Location"}
            "th"->{ mDialogView.titleInfo.text = ": ได้ล้มลงไปสู่พื้น"
                mDialogView.callAmbulance.text = "โทรเรียกรถพยาบาล"
                mDialogView.getLocation.text = "ขอตำแหน่งที่อยู่ปัจจุบัน"}
        }
        mDialogView.username.text = name

        mDialogView.callAmbulance.setOnClickListener{
            Log.i("dialog","click call Ambulance ja")
            dialog.dismiss()
            val emergency = "1112"
            val intent = Intent(Intent.ACTION_CALL, Uri.fromParts("tel", emergency, null))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        mDialogView.getLocation.setOnClickListener{
            Log.i("dialog","click get Location ja , phone :$phone")
            dialog.dismiss()

            database = Firebase.database.reference
            listener = database.child("users_blind/$phone/Device").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val locationFB = dataSnapshot.child("critical_Condition_Location").value.toString()
                        Log.i("dialog","location = $locationFB")
//                        location = locationFB
                        /** open google map */
                        // Navigation : current place direct to gmmIntentUri
                        val gmmIntentUri = Uri.parse("google.navigation:q=$locationFB&mode=w&avoid=thf")
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        mapIntent.setPackage("com.google.android.apps.maps")
                        startActivity(mapIntent)

                        database.child("users_blind/$phone/Device").removeEventListener(listener)

                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.d("checkUser_Main_FD","onCancelled call")
                }
            })

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

        sharedPrefLanguage = getSharedPreferences("value", 0)
        language = sharedPrefLanguage.getString("stringKey", "not found!").toString()

        when(language){
            "en"->{mDialogView.title.text = "Navigation has been set"
                mDialogView.subtitle.text = "Press to go back to the app."
               }
            "th"->{mDialogView.title.text = "ตั้งค่าการนำทางเสร็จสิ้น"
                mDialogView.subtitle.text = "แตะที่หน้าจอเพื่อกลับเข้าใช้งานแอปพลิเคชันอีกครั้ง."}
        }

        textToSpeech = TextToSpeech(this, TextToSpeech.OnInitListener { status ->
            if (status != TextToSpeech.ERROR) {
                textToSpeech!!.language = Locale.US
                textToSpeech!!.speak(getString(R.string.gotoApp_speak), TextToSpeech.QUEUE_FLUSH, null)
            } })
        textToSpeech!!.setSpeechRate(0.9f)

        mDialogView.gotoAppEvent.setOnClickListener{
            Log.i("alertDialogBack","click layout ja")
            dialog.dismiss()

            sharedPrefNavigate = getSharedPreferences("value", 0)
            val editor = sharedPrefNavigate.edit()
            editor.putString("stringKeyNavigate", "active")
            editor.apply()

            val launchIntent = packageManager.getLaunchIntentForPackage("com.estazo.project.seeable.app")
            startActivity(launchIntent)
        }

    }

}
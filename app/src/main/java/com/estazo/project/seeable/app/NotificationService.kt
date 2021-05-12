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

    var textToSpeech: TextToSpeech? = null

    private lateinit var database: DatabaseReference
    private lateinit var listener: ValueEventListener

    private lateinit var sharedPrefNavigate : SharedPreferences
    private lateinit var sharedPrefLanguage: SharedPreferences
    private lateinit var language : String
    private var phone: String = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        Log.i("notificationService"," call onCreate() -> create class NotificationService")

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

        if ( title != "null" && text != "null"){
            Log.d("notificationServiceInfo", "Package : $pack , Title : $title , Text : $text" +
                    ", regEX -> matchResult : $matchResult , checkNavigation : $checkNavigation")
            val msgNS = Intent("Msg")
            msgNS.putExtra("package", pack)
            msgNS.putExtra("title", title)
            msgNS.putExtra("text", text)
            LocalBroadcastManager.getInstance(context).sendBroadcast(msgNS)
            if(pack == "com.estazo.project.seeable.app" && checkCritical == "Critical"){
                Log.d("notificationServiceInfo", "call critical alertDialog")
                val info = text.substring(5)
                phone = info

                val nameInTitle = title.split(":".toRegex()).toTypedArray()
                val name  = nameInTitle[0]
                Log.d("notificationServiceInfo","nameInTitle :$nameInTitle , name : $name")
                alertDialogCritical(name)

            }
            if(pack == "com.estazo.project.seeable.app" && checkNavigation == "navigation"){
                Log.d("notificationServiceInfo", "call go back alertDialog")
                alertDialogBack()
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        Log.i("notificationService", "Notification Removed")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("notificationService"," call destroy")
    }

    private fun alertDialogCritical(name: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.alert_dialog_critical_event, null)
        dialogBuilder.setView(mDialogView)

        Log.d("dialogNS"," call alertDialogCritical")
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
            Log.d("dialogNS_C","click call Ambulance ")
            dialog.dismiss()
            val emergency = "1112"
            val intent = Intent(Intent.ACTION_CALL, Uri.fromParts("tel", emergency, null))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        mDialogView.getLocation.setOnClickListener{
            Log.d("dialogNS_C","click get Location -> phone :$phone")
            dialog.dismiss()

            database = Firebase.database.reference
            listener = database.child("users_blind/$phone/Device").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val locationFB = dataSnapshot.child("critical_Condition_Location").value.toString()
                        Log.i("dialog","location = $locationFB")
                        /** open google map */
                        val gmmIntentUri = Uri.parse("google.navigation:q=$locationFB&mode=w&avoid=thf")
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        mapIntent.setPackage("com.google.android.apps.maps")
                        startActivity(mapIntent)

                        database.child("users_blind/$phone/Device").removeEventListener(listener)

                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.d("dialogNS","onCancelled call")
                }
            })

        }

    }

    private fun alertDialogBack() {
        val dialogBuilder = AlertDialog.Builder(this)
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.alert_dialog_go_to_app, null)
        dialogBuilder.setView(mDialogView)

        Log.d("dialogNS","call alertDialogBack")
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
            Log.d("dialogNS_GBA","click layout ja")
            dialog.dismiss()

            sharedPrefNavigate = getSharedPreferences("value", 0)
            val editor = sharedPrefNavigate.edit()
            editor.putString("dialogNS", "active")
            editor.apply()

            val launchIntent = packageManager.getLaunchIntentForPackage("com.estazo.project.seeable.app")
            startActivity(launchIntent)
        }

    }

}
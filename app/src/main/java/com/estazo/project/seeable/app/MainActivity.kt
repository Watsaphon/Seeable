package com.estazo.project.seeable.app

import android.app.AlertDialog
import android.content.Context
import android.os.*
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.fall_dectection_dialog.view.*
import java.util.*
import androidx.lifecycle.Observer


class MainActivity : AppCompatActivity() {

    private var title = ""
    private var message = ""
    private lateinit var  mAlertDialog : AlertDialog
    var textToSpeech: TextToSpeech? = null

    var readRef: DatabaseReference? = null
    var listener: ValueEventListener? = null

    private val viewModel : UserTypeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hideSystemUI()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        Log.i("MainActivity", "onCreate called ")

        val sharedPrefPhone = getSharedPreferences("value", 0)
        val phone = sharedPrefPhone.getString("stringKeyPhone","not found!")
        val editorPhone = sharedPrefPhone.edit()
        val sharedPrefUserType = getSharedPreferences("value", 0)
        val userType = sharedPrefUserType.getString("stringKeyType","not found!")
        val editor3 = sharedPrefUserType.edit()

        viewModel.userType.observe(this, Observer<String> { typeView ->
            Log.d("eieiei_Main","type : $typeView")
        })

        Log.i("fff","userType : $userType")
//        when (userType){
//            "not found!" -> { Toast.makeText(this, "null", Toast.LENGTH_LONG).show() }
//            "blind" -> { Toast.makeText(this, "blind", Toast.LENGTH_LONG).show() }
//            "caretaker" -> { Toast.makeText(this, "caretaker", Toast.LENGTH_LONG).show() }
//        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d("testNotification", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            // Log and toast
            val msg = getString(R.string.msg_token_fmt, token)
            Log.d("testNotification", msg)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })

//        Firebase.messaging.subscribeToTopic("shared_location").addOnCompleteListener { task ->
//            var msg = getString(R.string.msg_subscribed)
//            if (!task.isSuccessful) {
//                msg = getString(R.string.msg_subscribe_failed)
//            }
//            Log.d("testNotification", msg)
//            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
//        }
//
//        Firebase.messaging.subscribeToTopic("call_emergency").addOnCompleteListener { task ->
//            var msg = "Subscribed to call emergency topic"
//            if (!task.isSuccessful) {
//                msg = "Failed to subscribe to call Emergency topic"
//            }
//            Log.d("testNotification", msg)
//            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
//        }
//
//        Firebase.messaging.subscribeToTopic("self-navigate").addOnCompleteListener { task ->
//            var msg = "Subscribed to Self Navigate topic"
//            if (!task.isSuccessful) {
//                msg = "Failed to subscribe to Self Navigate topic"
//            }
//            Log.d("testNotification", msg)
//            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
//        }

    }

    override fun onStart() {
        super.onStart()
        Log.i("MainActivity", "onStart called")
    }

    override fun onResume() {
        super.onResume()
        Log.i("MainActivity", "onResume called")
        updateUI()
        val sharedPrefPhone = getSharedPreferences("value", 0)
        val phone = sharedPrefPhone.getString("stringKeyPhone","not found!")
        val editorPhone = sharedPrefPhone.edit()
        val sharedPrefUserType = getSharedPreferences("value", 0)
        val userType = sharedPrefUserType.getString("stringKeyType","not found!")
        val editorType = sharedPrefUserType.edit()
        when (userType){
            "not found!" -> {
                Toast.makeText(this, "null", Toast.LENGTH_LONG).show()
//                readRef!!.removeEventListener(listener!!)
//                editorType.putString("stringKeyType", "not found!")
//                editorType.apply()
//                viewModel.userType.value = "not found!"
            }
            "blind" -> {
                Toast.makeText(this, "blind", Toast.LENGTH_LONG).show()
//                viewModel.userType.value = "blind"
//                editorType.putString("stringKeyType", "blind")
//                editorType.apply()

                /**  test */
//                readRef = FirebaseDatabase.getInstance().getReference("users_blind/$phone/")
//                listener = readRef!!.child("Device").addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                    val fallDetections = dataSnapshot.child("fall_Detection").value.toString()
//                    Log.d("testFD","fallDetections : $fallDetections")
//                }
//                override fun onCancelled(error: DatabaseError) {
//                    Log.d("testFD","onCancelled call")
//                }
//            })

//                val firebaseRef = FirebaseDatabase.getInstance().getReference("users_blind/$phone/Device")
//                firebaseRef.addValueEventListener(object : ValueEventListener {
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        val fallDetections = snapshot.child("fall_Detection").value.toString()
//                        if(fallDetections == "fall" ){
//                            alertDialogFallDetection()
//                        }
//                        Log.i("testFD","fallDetections : $fallDetections")
//                    }
//                    override fun onCancelled(databaseError: DatabaseError) {
//                    }
//                })
//                readRef!!.removeEventListener(listener!!)
            }
            "caretaker" -> {
                Toast.makeText(this, "caretaker", Toast.LENGTH_LONG).show()
//                viewModel.userType.value = "caretaker"
//                editorType.putString("stringKeyType", "caretaker")
//                editorType.apply()
            }
        }
//        if(userType != "blind" && readRef != null){
//            readRef!!.removeEventListener(listener!!)
//            Log.d("eieiei","kuy")
//        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        hideSystemUI()
        Log.d("MainActivity", "onWindowFocusChanged called")
    }

    /** hide navigation and status bar in each activity */
    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    private fun updateUI() {
        val decorView = window.decorView
        decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            }
        }
    }

    fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun alertDialogFallDetection() {
        //Inflate the dialog with custom view
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.fall_dectection_dialog, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
        //show dialog
        mAlertDialog  = mBuilder.show()
        mAlertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        mAlertDialog.setCanceledOnTouchOutside(false)
        mAlertDialog.setCancelable(false)
        textToSpeech = TextToSpeech(this, TextToSpeech.OnInitListener { status ->
            if (status != TextToSpeech.ERROR) {
                textToSpeech!!.language = Locale.US
                textToSpeech!!.speak(getString(R.string.fallDetection_info2), TextToSpeech.QUEUE_FLUSH, null);
            }
        })
        textToSpeech!!.setSpeechRate(0.9f)

        mDialogView.fallDetection.setOnVeryLongClickListener{
            vibrate()
            val tts = getString(R.string.fallDetection_fine)
            textToSpeech!!.speak(tts, TextToSpeech.QUEUE_FLUSH, null)
            mAlertDialog.dismiss()
        }

    }

    /** function press and hold button for few seconds */
    private fun View.setOnVeryLongClickListener(listener: () -> Unit) {
        setOnTouchListener(object : View.OnTouchListener {
            private val longClickDuration = 2000L
            private val handler = Handler()
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event?.action == MotionEvent.ACTION_DOWN) {
                    handler.postDelayed({ listener.invoke() }, longClickDuration)
                } else if (event?.action == MotionEvent.ACTION_UP) {
                    handler.removeCallbacksAndMessages(null)
                }
                return true
            }
        })
    }

    /** function vibrations */
    private fun vibrate(){
        // Vibrate for 300 milliseconds
        val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE))
        }
        else {
            //deprecated in API 26
            v.vibrate(300)
        }
    }

}
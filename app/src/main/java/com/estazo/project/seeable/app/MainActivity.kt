package com.estazo.project.seeable.app

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
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
import androidx.lifecycle.Observer
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.alert_dialog_fall_dectection.view.*
import java.util.*


class MainActivity : AppCompatActivity() {


    private lateinit var  mAlertDialog : AlertDialog
    private var textToSpeech: TextToSpeech? = null

    private lateinit var database: DatabaseReference
    private lateinit var listener: ValueEventListener

    private lateinit var sharedPrefPhone: SharedPreferences

    private val viewModel : UserTypeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hideSystemUI()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        Log.i("MainActivity", "onCreate called ")


        viewModel.userType.observe(this, Observer<String> { typeView ->
            Log.d("checkUser_Main","type : $typeView")
            when(typeView){
                "not found!" -> { Toast.makeText(this, "Login Section",Toast.LENGTH_LONG).show() }
                "caretaker" -> {

                    sharedPrefPhone = getSharedPreferences("value", 0)
                    val phone = sharedPrefPhone.getString("stringKeyPhone", "not found!").toString()
                    Toast.makeText(this, "Caretaker Section",Toast.LENGTH_LONG).show()
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
                        val ref = FirebaseDatabase.getInstance().reference
                        val childUpdates = hashMapOf<String, Any>("users_caretaker/$phone/FCM" to "$token")
                        ref.updateChildren(childUpdates)
                    })
                }
                "blind" -> {
                    blindSection()
                    Toast.makeText(this, "Blind Section",Toast.LENGTH_LONG).show()
                }
            }
        })


    }

    override fun onStart() {
        super.onStart()
        Log.i("MainActivity", "onStart called")
    }

    override fun onResume() {
        super.onResume()
        Log.i("MainActivity", "onResume called")
        updateUI()

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
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.alert_dialog_fall_dectection, null)
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

    private fun blindSection(){

        val sharedPrefPhone = getSharedPreferences("value", 0)
        val phone = sharedPrefPhone.getString("stringKeyPhone","not found!")

        viewModel.userType.observe(this, Observer<String> { typeView ->
            Log.d("checkUser_Main_BS","type : $typeView")
            when(typeView){
                "not found!" -> {
                    if(this::database.isInitialized){
                        database.child("users_blind/$phone/Device").removeEventListener(listener)
                        Log.d("checkUser_Main_BS","logout to Login section")
                    }
                }
                "caretaker" -> {
                    if(this::database.isInitialized){
                        database.child("users_blind/$phone/Device").removeEventListener(listener)
                        Log.d("checkUser_Main_BS","login to caretaker section")
                    }
                }

            }
        })
        /**For java*/
//        database = FirebaseDatabase.getInstance().reference
        /**For kotlin*/
//        database = Firebase.database.reference
        database = Firebase.database.reference
        listener = database.child("users_blind/$phone/Device").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                val fallDetections = dataSnapshot.child("fall_Detection").value.toString()
                Log.d("checkUser_Main_FD", "fallDetections : $fallDetections")
                    if (fallDetections == "fall") {
                        alertDialogFallDetection()
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("checkUser_Main_FD","onCancelled call")
            }
        })
    }

}
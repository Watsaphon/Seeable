package com.estazo.project.seeable.app


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.net.Uri
import android.os.*
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.graphics.green
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var button1: Button
    private lateinit var button2: Button
    private lateinit var button3: Button
    private lateinit var button4: Button
    private lateinit var fab: FloatingActionButton
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferences2: SharedPreferences
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var personName : String
    private lateinit var personEmail : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hideSystemUI()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)


        sharedPreferences = getSharedPreferences("value", 0)
        sharedPreferences2 = getSharedPreferences("value", 0)


        val stringValue = sharedPreferences.getString("stringKey", "not found!")
        val stringValue2 = sharedPreferences2.getString("stringKey2", "not found!")

       Log.i("SplashScreenMain", "Current User ID : $stringValue2")
        Log.i("SplashScreenMain", "LoginScreen now language : $stringValue")


        button1 = findViewById(R.id.button1)
        button2 = findViewById(R.id.button2)
        button3 = findViewById(R.id.button3)
        button4 = findViewById(R.id.button4)
        fab = findViewById(R.id.floating_action_button)

        button1.setOnVeryLongClickListener{
            vibrate()
            Toast.makeText(this,getString(R.string.button_main_1), Toast.LENGTH_SHORT).show()
        }
        button2.setOnVeryLongClickListener{
            vibrate()
            Toast.makeText(this, getString(R.string.button_main_2), Toast.LENGTH_SHORT).show()
        }
        button3.setOnVeryLongClickListener{
            vibrate()
            Toast.makeText(this, getString(R.string.button_main_3), Toast.LENGTH_SHORT).show()
        }
        button4.setOnVeryLongClickListener{
            vibrate()
            Toast.makeText(this, getString(R.string.button_main_4), Toast.LENGTH_SHORT).show()
        }
        fab.setOnClickListener {
            /** PopupMenu dropdown */
            val popupMenu = PopupMenu(this, fab, Gravity.CENTER)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
            popupMenu.menu.findItem(R.id.action_logout).isVisible = true
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_about -> gotoAbout()
                    R.id.action_change_language -> changeLanguage()
                    R.id.action_settings -> gotoSetting()
                    R.id.action_logout -> gotoLogout()
                }
                hideSystemUI()
                true
            }
            popupMenu.show()
        }
    }

    override fun onResume(){
        super.onResume()
        Log.i("MainActivity", "onResume called")
    }

    override fun onPause() {
        super.onPause()
        Log.i("MainActivity", "onPause called")
    }

    override fun onStop() {
        super.onStop()
        Log.i("MainActivity", "onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("MainActivity", "onDestroy called")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
        Log.i("MainActivity", "onBackPressed called")
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        hideSystemUI()
        Log.i("MainActivity", "onWindowFocusChanged called")
    }

    /** change Language TH and EN*/
    private fun changeLanguage(){
        val language = sharedPreferences.getString("stringKey", "not found!")
        Log.i("SplashScreenMain", "Now Language is :$language ")
        var locale: Locale? = null
        var editor = sharedPreferences.edit()
        if (language=="en") {
            locale = Locale("th")
            editor.putString("stringKey", "th")
            editor.apply()
        } else if (language =="th") {
            locale = Locale("en")
            editor.putString("stringKey", "en")
            editor.apply()
        }
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, null)
        val intent = Intent(this,SplashScreen::class.java)
        startActivity(intent)
//        recreate()
    }

    private fun gotoAbout(){
        val intent = Intent(this,AboutScreen::class.java)
        startActivity(intent)
    }

    private fun gotoSetting(){
        val intent = Intent(this,SettingScreen::class.java)
        startActivity(intent)
    }

    private fun gotoLogout(){
         val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if(acct != null){
            mGoogleSignInClient.signOut()
            Toast.makeText(this, getString(R.string.action_logout), Toast.LENGTH_SHORT).show()
        }
        var editor2 = sharedPreferences2.edit()
        editor2.putString("stringKey2", "not found!")
        editor2.apply()
        val intent = Intent(this,LoginScreen::class.java)
        startActivity(intent)
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



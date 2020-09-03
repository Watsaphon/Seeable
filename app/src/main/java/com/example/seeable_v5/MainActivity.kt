package com.example.seeable_v5


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.*
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.View.OnLongClickListener
import android.view.View.OnTouchListener
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*


class MainActivity : AppCompatActivity(){


    private lateinit var button1: Button
    private lateinit var button2: Button
    private lateinit var button3: Button
    private lateinit var button4: Button
    private lateinit var fab: FloatingActionButton
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hideSystemUI()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)



        sharedPreferences = getSharedPreferences("value", 0)
        var editor = sharedPreferences.edit()
        editor.putBoolean("FinishedInformation", true)
        editor.commit()


        val stringValue = sharedPreferences.getString("stringKey", "not found!")
        val booleanValue = sharedPreferences.getBoolean("FinishedInformation", false)

        Log.i("SplashScreenMain", "MainAc now language : $stringValue")
        Log.i("MainActivity ", "Boolean value: $booleanValue")

        button1 = findViewById(R.id.button1)
        button2 = findViewById(R.id.button2)
        button3 = findViewById(R.id.button3)
        button4 = findViewById(R.id.button4)

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

//        button1.setOnTouchListener(buttonOnTouchListener)
//        button2.setOnTouchListener(buttonOnTouchListener)
//        button3.setOnTouchListener(buttonOnTouchListener)
//        button4.setOnTouchListener(buttonOnTouchListener)



        fab = findViewById(R.id.floating_action_button)
        fab.setOnClickListener {
            /** PopupMenu dropdown */
            val popupMenu = PopupMenu(this, fab, Gravity.CENTER)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_about -> gotoAbout()
                    R.id.action_change_language -> changeLanguage()
                    R.id.action_settings -> gotoSetting()
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



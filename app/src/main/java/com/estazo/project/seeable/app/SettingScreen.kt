package com.estazo.project.seeable.app

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import java.util.*

class SettingScreen : AppCompatActivity() {

    private lateinit var closeButton: Button
    private lateinit var aboutButton: Button
    private lateinit var changeLanguageButton: Button
    private lateinit var otherButton: Button
    private lateinit var sharedPrefLanguage : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_screen)
        hideSystemUI()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        sharedPrefLanguage = getSharedPreferences("value", 0)
        var editor = sharedPrefLanguage.edit()
        editor.commit()


        val stringValue = sharedPrefLanguage.getString("stringKey", "not found!")
        Log.i("SettingScreen", "String value -> change language to : $stringValue")
//        val booleanValue = sharedPreferences.getBoolean("FinishedInformation", false)
//          Log.i("SettingScreen ", "Boolean value: $booleanValue")

        closeButton = findViewById(R.id.close_button)
        aboutButton = findViewById(R.id.action_about)
        changeLanguageButton = findViewById(R.id.action_change_language)
        otherButton = findViewById(R.id.action_settings)

        closeButton.setOnClickListener {
        onBackPressed()
        }

        aboutButton.setOnClickListener{
            gotoAbout()
        }

        changeLanguageButton.setOnClickListener{
            changeLanguage()
            val intent = Intent(this,SplashScreen::class.java)
            startActivity(intent)
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        hideSystemUI()
        Log.i("MainActivity", "onWindowFocusChanged called")
    }

    private fun gotoAbout(){
        val intent = Intent(this,AboutScreen::class.java)
        startActivity(intent)
    }

    private fun changeLanguage(){
        val language = sharedPrefLanguage.getString("stringKey", "not found!")
        Log.i("MainActivity", "Now Language is :$language ")
        var locale: Locale? = null
        var editor = sharedPrefLanguage.edit()
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
//        recreate()
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

}
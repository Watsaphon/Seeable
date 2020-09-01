package com.example.seeable_v5

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import java.util.*

class SettingScreen : AppCompatActivity() {

    private lateinit var closeButton: Button
    private lateinit var aboutButton: Button
    private lateinit var changeLanguageButton: Button
    private lateinit var otherButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_screen)

        sharedPreferences = getSharedPreferences("value", 0)
        var editor = sharedPreferences.edit()
        editor.commit()


        val stringValue = sharedPreferences.getString("stringKey", "not found!")
        val booleanValue = sharedPreferences.getBoolean("FinishedInformation", false)
        Log.i("SettingScreen", "String value -> change language to : $stringValue")
        Log.i("SettingScreen ", "Boolean value: $booleanValue")

        closeButton = findViewById(R.id.close_button)
        aboutButton = findViewById(R.id.action_about)
        changeLanguageButton = findViewById(R.id.action_change_language)
        otherButton = findViewById(R.id.action_settings)

        closeButton.setOnClickListener {
        onBackPressed()
        }
        changeLanguageButton.setOnClickListener{
            changeLanguage()
            val intent = Intent(this,SplashScreen::class.java)
            startActivity(intent)
        }
    }

    private fun changeLanguage(){
        val language = sharedPreferences.getString("stringKey", "not found!")
        Log.i("MainActivity", "Now Language is :$language ")
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
//        recreate()
    }
}
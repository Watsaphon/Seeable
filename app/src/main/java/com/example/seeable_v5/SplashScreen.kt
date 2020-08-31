package com.example.seeable_v5

import android.content.Intent
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity


class SplashScreen : AppCompatActivity() {
    private val MY_PREFS = "my_prefs"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//      setContentView(R.layout.activity_splash_screen)
        hideSystemUI()

        /**Shared Preferences เป็นคลาสที่ใช้สำหรับเก็บข้อมูลถาวรที่เป็นค่าของตัวแปรธรรมดาๆ อย่างเช่น Boolean,Int,Float*/
        val sharedPreferences = getSharedPreferences("first", 0)

        Log.i("sharedPref_splash",sharedPreferences.toString())

        if (sharedPreferences.getBoolean("FinishedInformation", false)) {
            Handler().postDelayed({
                startActivity(Intent(this, MainActivity::class.java))
                finishAffinity()
            }, 2000)

        } else {
//            sharedPreferences.edit().clear()
//            sharedPreferences.edit().apply()
            Handler().postDelayed({
                startActivity(Intent(this, SelectRegister::class.java))
                finishAffinity()
            }, 2000)
        }
//        var stringValue = sharedPreferences.getString("stringKey", "not found!")
//        var  booleanValue = sharedPreferences.getBoolean("booleanKey", false)
//        // Save SharedPreferences
//        val editor: Editor = shared.edit()
//        editor.putString("stringKey", "This is a book!")
//        editor.putBoolean("booleanKey", true)
//        editor.commit()
//        Log.i("LOG_TAG", "String value: $stringValue")
//        Log.i("LOG_TAG ", "Boolean value: $booleanValue")

    }

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
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                )
    }
}

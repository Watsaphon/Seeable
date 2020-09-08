package com.estazo.project.seeable.app

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//      setContentView(R.layout.activity_splash_screen)
        hideSystemUI()

        /**Shared Preferences เป็นคลาสที่ใช้สำหรับเก็บข้อมูลถาวรที่เป็นค่าของตัวแปรธรรมดาๆ อย่างเช่น Boolean,Int,Float*/
        val sharedPreferences = getSharedPreferences("value", 0)
        val language = sharedPreferences.getString("stringKey", "en")
        var editor = sharedPreferences.edit()
        var locale: Locale? = null

        Log.i("SplashScreen", "First Language is :$language")

        /** Check Language in app*/

        if(language=="en"){
            locale = Locale("en")
            Locale.setDefault(locale)
            editor.putString("stringKey", "en")
            Log.i("SplashScreen", " Language after check if :$language")
        }
        else if(language=="th"){
            locale = Locale("th")
            Locale.setDefault(locale)
            editor.putString("stringKey", "th")
            Log.i("SplashScreen", " check else-if : Now Language :$language")
        }
        editor.apply()
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, null)
        Log.i("SplashScreen", "Now Language is :$language")

        /** Check First Register user in app*/

        if (sharedPreferences.getBoolean("FinishedInformation", false)) {
            Handler().postDelayed({
                startActivity(Intent(this, SigninByGmail::class.java))
                finishAffinity()
            }, 2000)

        } else {
            Handler().postDelayed({
                startActivity(Intent(this, SelectRegister::class.java))
                finishAffinity()
            }, 2000)
        }
    }

    private fun hideSystemUI() {
        /**
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
         */
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

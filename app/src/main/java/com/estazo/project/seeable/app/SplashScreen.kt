package com.estazo.project.seeable.app

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.estazo.project.seeable.app.Login.LoginScreen
import com.estazo.project.seeable.app.Register.SelectRegister
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*


var checkSuccess : Boolean = false

class SplashScreen : AppCompatActivity() {
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideSystemUI()
        Log.i("test", "onCreate() call")

        /**Shared Preferences เป็นคลาสที่ใช้สำหรับเก็บข้อมูลถาวรที่เป็นค่าของตัวแปรธรรมดาๆ อย่างเช่น Boolean,Int,Float*/
        val sharedPrefIntroApp = getSharedPreferences("value", 0)
        val introApp = sharedPrefIntroApp.getString("stringKeyIntro", "No")


        val sharedPrefLanguage = getSharedPreferences("value", 0)
        val language = sharedPrefLanguage.getString("stringKey", "en")
        val editor = sharedPrefLanguage.edit()

        val sharedPrefID = getSharedPreferences("value", 0)
        val login = sharedPrefID.getString("stringKey2","not found!")
        val editor2 = sharedPrefID.edit()

        val sharedPrefUserType = getSharedPreferences("value", 0)
        val userType = sharedPrefUserType.getString("stringKeyType","not found!")
        val editor3 = sharedPrefUserType.edit()

        val sharedPrefGoogle = getSharedPreferences("value", 0)
        val userGoogle = sharedPrefGoogle.getString("stringKeyGoogle","not found!")
        var editorGoogleUser = sharedPrefGoogle.edit()

        val sharedGooglePrefUserType = getSharedPreferences("value", 0)
        val googleUserType = sharedGooglePrefUserType.getString("stringKeyGoogleType","not found!")
        var editorGoogleUserType = sharedPrefGoogle.edit()

        var locale: Locale? = null

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail().build()
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        Log.i("CheckUserID_splash", "First User ID  : $login")
        Log.i("CheckLanguage_splash", "First Language is :$language")

        /** Check Language in app*/
        if(language=="en"){
            locale = Locale("en")
            Locale.setDefault(locale)
            editor.putString("stringKey", "en")
            Log.i("CheckLanguage_splash", "  check if :  Language is$language")
        }
        else if(language=="th"){
            locale = Locale("th")
            Locale.setDefault(locale)
            editor.putString("stringKey", "th")
            Log.i("CheckLanguage_splash", " check else-if :  Language is $language")
        }
        editor.apply()
        editor2.apply()
        editor3.apply()

        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, null)
        Log.i("CheckUserID_splash", "  Second User  : $login")
        Log.i("CheckLanguage_splash", "Now Language is :$language")
        Log.i("CheckUserTypes_plash", "Now User Type is :$userType")

        Log.i("checkStatus"," login : $login , userType : $userType , userGoogle : $userGoogle , googleUserType : $googleUserType ")
        Log.i("CheckFirst_splash", "First Login :$introApp")
        if(introApp == "No"){
            Handler().postDelayed({
                startActivity(Intent(this@SplashScreen, IntroduceApp::class.java))
                finishAffinity()
            }, 1000)
        }
       else{
            if(login != "not found!" && userType != "not found!" ){
                if(userType== "caretaker"){
                    Handler().postDelayed({
                        startActivity(Intent(this@SplashScreen, MainActivityPerson::class.java))
                        finishAffinity()
                    }, 1000)
                }
                else if (userType== "blind"){
                    Handler().postDelayed({
                        startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                        finishAffinity()
                    }, 1000)
                }
            }
            else if(userGoogle != "not found!" && googleUserType != "not found!" ){
                when (googleUserType) {
                    "caretaker" -> {
                        Handler().postDelayed({
                            startActivity(Intent(this@SplashScreen, MainActivityPerson::class.java))
                            finishAffinity()
                        }, 1000)
                    }
                    "blind" -> {
                        Handler().postDelayed({
                            startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                            finishAffinity()
                        }, 1000)
                    }
                    "noRegister" -> {
                        Handler().postDelayed({
                            startActivity(Intent(this@SplashScreen, SelectRegister::class.java))
                            finishAffinity()
                        }, 1000)
                    }
                }
            }
            else{
                Handler().postDelayed({
                    startActivity(Intent(this@SplashScreen, LoginScreen::class.java))
                    finishAffinity()
                }, 1000)
            }
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

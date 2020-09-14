package com.estazo.project.seeable.app

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.estazo.project.seeable.app.Login.LoginScreen
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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

        val sharedPreferences2 = getSharedPreferences("value", 0)
        val login = sharedPreferences2.getString("stringKey2","not found!")
        var editor2 = sharedPreferences2.edit()

        var locale: Locale? = null

        Log.i("SplashScreen", "First User ID  : $login")
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
        editor2.apply()
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, null)
        Log.i("SplashScreen", "  Second User  : $login")
        Log.i("SplashScreen", "Now Language is :$language")

        /** Check User for Login */
        if(login=="not found!"){
            Log.i("SplashScreen", " Current User ID  : $login")
            Handler().postDelayed({
                startActivity(Intent(this, LoginScreen::class.java))
                finishAffinity()
            }, 2000)
        }
        else if(login != null){
            Log.i("SplashScreen", "Current User ID : $login")
//            Handler().postDelayed({
//                startActivity(Intent(this, MainActivity::class.java))
//                finishAffinity()
//            }, 2000)
            checkLogin()
        }


    }

    private fun checkLogin() {
        val query = FirebaseDatabase.getInstance().getReference("users_person").orderByChild("id")
        query.addListenerForSingleValueEvent(valueEventListener)
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

    /**receive value from realtime database (user_person) and check Login */
    private var valueEventListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val sharedPreferences2 = getSharedPreferences("value", 0)
            val login = sharedPreferences2.getString("stringKey2","not found!")
            if (dataSnapshot.exists()) {
                for (snapshot in dataSnapshot.children) {
                    val id = snapshot.child("id").value.toString()
                    Log.i("LoginScreen_checkperson", "Username : $login")
                    Log.i("LoginScreen_checkperson", "Database info :  $id")
                    if (login.equals(id)){
                        Handler().postDelayed({
                            startActivity(Intent(this@SplashScreen, MainActivityPerson::class.java))
                            finishAffinity()
                        }, 2000)
                    }
                    else{
                        val query2 = FirebaseDatabase.getInstance().getReference("users_blind").orderByChild("id")
                        query2.addListenerForSingleValueEvent(valueEventListener2)
                    }
                }
            }
        }
        override fun onCancelled(databaseError: DatabaseError) {}
    }


    /**receive value from realtime database (users_blind) and check Login */
    private var valueEventListener2: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val sharedPreferences2 = getSharedPreferences("value", 0)
            val login = sharedPreferences2.getString("stringKey2","not found!")
            if (dataSnapshot.exists()) {
                for (snapshot in dataSnapshot.children) {
                    val id = snapshot.child("id").value.toString()
                    Log.i("LoginScreen_checkblind", "Username : $login")
                    Log.i("LoginScreen_checkblind", "Database info :  $id")

                    if (login.equals(id)) {
                        Handler().postDelayed({
                            startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                            finishAffinity()
                        }, 2000)
                    }
                }
            }
        }
        override fun onCancelled(databaseError: DatabaseError) {}
    }

}

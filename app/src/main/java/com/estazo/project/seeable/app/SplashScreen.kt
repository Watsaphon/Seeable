package com.estazo.project.seeable.app

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
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

//    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideSystemUI()
        Log.i("test", "onCreate() call")

        /**Shared Preferences เป็นคลาสที่ใช้สำหรับเก็บข้อมูลถาวรที่เป็นค่าของตัวแปรธรรมดาๆ อย่างเช่น Boolean,Int,Float*/
        val sharedPrefLanguage = getSharedPreferences("value", 0)
        val language = sharedPrefLanguage.getString("stringKey", "en")
        var editor = sharedPrefLanguage.edit()

        val sharedPrefID = getSharedPreferences("value", 0)
        val login = sharedPrefID.getString("stringKey2","not found!")
        var editor2 = sharedPrefID.edit()

        val sharedPrefUserType = getSharedPreferences("value", 0)
        val userType = sharedPrefUserType.getString("stringKeyType","not found!")
        var editor3 = sharedPrefUserType.edit()

        val sharedPrefGoogle = getSharedPreferences("value", 0)
        val userGoogle = sharedPrefGoogle.getString("stringKeyGoogle","not found!")
        var editorGoogleUser = sharedPrefGoogle.edit()

        var locale: Locale? = null

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

        Log.i("checkStatus","userGoogle : $userGoogle ,userType : $userType ,login : $login ")
        if(login != "not found!"){
            if(userType== "person"){
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
        else if(userGoogle != "not found!"){
            if(userGoogle != "not found!" && userGoogle == "not register!!"){
                Log.i("testusergoogle","userGoogle : $userGoogle ,userType : $userType ")
                Handler().postDelayed({
                    startActivity(Intent(this@SplashScreen, SelectRegister::class.java))
                    finishAffinity()
                }, 1000)
            }
            else{
                Log.i("testusergoogle","userGoogle : $userGoogle ,userType : $userType ")
                if(userType== "person"){
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
        }
        else{
            Handler().postDelayed({
                startActivity(Intent(this@SplashScreen, LoginScreen::class.java))
                finishAffinity()
            }, 1000)
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

    //    private fun checkLogin() {
//        val query = FirebaseDatabase.getInstance().getReference("users_person").orderByChild("id")
//        query.addListenerForSingleValueEvent(valueEventListener)
//    }

//    /**receive value from realtime database (user_person) and check Login */
//    private var valueEventListener: ValueEventListener = object : ValueEventListener {
//        override fun onDataChange(dataSnapshot: DataSnapshot) {
//            val sharedPrefID = getSharedPreferences("value", 0)
//            val login = sharedPrefID.getString("stringKey2","not found!")
//            //if(!dataSnapshot.child("users").child("email").exist)
//            if (dataSnapshot.exists()) {
//                for (snapshot in dataSnapshot.children) {
//                    val id = snapshot.child("id").value.toString()
//                    Log.i("CheckUserPerson_splash", "Username : $login")
//                    Log.i("CheckUserPerson_splash", "User from Database :  $id")
//                    if (login.equals(id)){
//                        /** Check user pair with blinder */
//                        val query = FirebaseDatabase.getInstance().getReference("users_person").child("$login").orderByChild("partner_id")
//                        query.addListenerForSingleValueEvent(valueEventListenerCheckUser)
//                        Handler().postDelayed({
//                            startActivity(Intent(this@SplashScreen, MainActivityPerson::class.java))
//                            finishAffinity()
//                        }, 1000)
//                        checkSuccess = true
//                        break
//                    }
//                }
//                val query2 = FirebaseDatabase.getInstance().getReference("users_blind").orderByChild("id")
//                query2.addListenerForSingleValueEvent(valueEventListener2)
//            }
//        }
//        override fun onCancelled(databaseError: DatabaseError) {}
//    }


//    /**receive value from realtime database (users_blind) and check Login */
//    private var valueEventListener2: ValueEventListener = object : ValueEventListener {
//        override fun onDataChange(dataSnapshot: DataSnapshot) {
//            val sharedPrefID = getSharedPreferences("value", 0)
//            val login = sharedPrefID.getString("stringKey2","not found!")
//            if(checkSuccess == false){
//                if (dataSnapshot.exists()) {
//                    for (snapshot in dataSnapshot.children) {
//                        val id = snapshot.child("id").value.toString()
//                        Log.i("CheckUserPerson_splash", "Username : $login")
//                        Log.i("CheckUserPerson_splash", "User from Database :  $id")
//                        if (login.equals(id)) {
//                            Handler().postDelayed({
//                                startActivity(Intent(this@SplashScreen, MainActivity::class.java))
//                                finishAffinity()
//                            }, 1000)
//                            break
//                        }
//                    }
//                }
//            }
//        }
//        override fun onCancelled(databaseError: DatabaseError) {}
//    }

//    /** Check User pair with blinder */
//    private var valueEventListenerCheckUser: ValueEventListener = object : ValueEventListener {
//        @RequiresApi(Build.VERSION_CODES.O)
//        override fun onDataChange(dataSnapshot: DataSnapshot) {
//            if (dataSnapshot.exists()) {
//                val partnerIDFirebase = dataSnapshot.child("partner_id").value.toString()
//                val sharedPrefPartnerID = getSharedPreferences("value", 0)
//                val partnerID = sharedPrefPartnerID.getString("stringKeyPartnerID","not found!")
//                var editorPartnerID = sharedPrefPartnerID.edit()
//                Log.i("checkPairing_splash"," Partner ID :$partnerIDFirebase")
//                if (partnerIDFirebase != "no-pairing") {
//                    Log.i("checkPairing_splash"," Partner ID :$partnerIDFirebase")
//                    editorPartnerID.putString("stringKeyPartnerID", "$partnerIDFirebase")
//                    editorPartnerID.apply()
//                }
//                else if(partnerIDFirebase== "no-pairing"){
//                    Log.i("checkPairing_splash"," Partner ID :$partnerIDFirebase")
//                    editorPartnerID.putString("stringKeyPartnerID", "no-pairing")
//                    editorPartnerID.apply()
//                }
//            }
//        }
//        override fun onCancelled(databaseError: DatabaseError) {}
//    }

}

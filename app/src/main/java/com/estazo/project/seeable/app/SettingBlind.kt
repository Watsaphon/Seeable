package com.estazo.project.seeable.app

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import java.util.*
import android.content.res.Configuration
import com.estazo.project.seeable.app.Login.LoginScreen
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth


class SettingBlind : AppCompatActivity() {

    private lateinit var backButton: ImageButton
    private lateinit var lang: Button
    private lateinit var account: Button
    private lateinit var setLocation: Button
    private lateinit var manageCare: Button
    private lateinit var deleteAccount: Button
    private lateinit var logout: Button

    private lateinit var sharedPrefLanguage: SharedPreferences
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private lateinit var sharedPrefID: SharedPreferences
    private lateinit var sharedPrefFullName: SharedPreferences
    private lateinit var sharedPrefNameHelper: SharedPreferences
    private lateinit var sharedPrefPassword: SharedPreferences
    private lateinit var sharedPrefPhone: SharedPreferences
    private lateinit var sharedPrefPhoneHelper: SharedPreferences
    private lateinit var sharedPrefSex: SharedPreferences
    private lateinit var sharedPrefUserType : SharedPreferences



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_blind)

        backButton = findViewById(R.id.back_button)
        lang = findViewById(R.id.langBtn)
        account = findViewById(R.id.accountBtn)
        setLocation = findViewById(R.id.locationBtn)
        manageCare = findViewById(R.id.manageUserBtn)
        deleteAccount = findViewById(R.id.deleteBtn)
        logout = findViewById(R.id.logoutBtn)

        sharedPrefLanguage = getSharedPreferences("value", 0)

        sharedPrefID = getSharedPreferences("value", 0)
        sharedPrefPhone= getSharedPreferences("value", 0)
        sharedPrefPassword= getSharedPreferences("value", 0)
        sharedPrefFullName= getSharedPreferences("value", 0)
        sharedPrefSex= getSharedPreferences("value", 0)
        sharedPrefNameHelper= getSharedPreferences("value", 0)
        sharedPrefPhoneHelper= getSharedPreferences("value", 0)
        sharedPrefUserType = getSharedPreferences("value", 0)



        lang.setOnClickListener {
            changeLanguage()
        }
        account.setOnClickListener {
            Toast.makeText(this@SettingBlind,"Account",Toast.LENGTH_SHORT).show()
        }
        setLocation.setOnClickListener {
            searchLocation()
            Toast.makeText(this@SettingBlind," set Location",Toast.LENGTH_SHORT).show()
        }
        manageCare.setOnClickListener {
            Toast.makeText(this@SettingBlind," manage Caretaker User",Toast.LENGTH_SHORT).show()
        }
        deleteAccount.setOnClickListener {
            Toast.makeText(this@SettingBlind," delete Account",Toast.LENGTH_SHORT).show()
        }
        logout.setOnClickListener {
            gotoLogout()
        }
        backButton.setOnClickListener {
            finish()
        }

    }

    private fun searchLocation(){
        val intent = Intent(this,SearchLocation::class.java)
        startActivity(intent)
    }



    /** change Language TH and EN*/
    private fun changeLanguage(){
        val language = sharedPrefLanguage.getString("stringKey", "not found!")
        Log.i("SplashScreenMain", "Now Language is :$language ")
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
        val intent = Intent(this,SplashScreen::class.java)
        startActivity(intent)
//        recreate()
    }

    private fun gotoLogout(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        val acct = GoogleSignIn.getLastSignedInAccount(this)
        Log.i("testusergoogle1","$acct")
        if(acct != null){
            mGoogleSignInClient.signOut()
            Toast.makeText(this, getString(R.string.action_logout), Toast.LENGTH_SHORT).show()
            Log.i("testusergoogle2","$acct")
        }

        FirebaseAuth.getInstance().signOut()

        val editorID = sharedPrefID.edit()
        val editorPhone = sharedPrefPhone.edit()
        val editorPassword = sharedPrefPassword.edit()
        val editorFullName = sharedPrefFullName.edit()
        val editorNameHelper = sharedPrefNameHelper.edit()
        val editorPhoneHelper = sharedPrefPhoneHelper.edit()
        val editorUserType = sharedPrefUserType.edit()
        val editorSex = sharedPrefSex.edit()

        editorID.putString("stringKey2", "not found!")
        editorPhone.putString("stringKeyPhone", "not found!")
        editorPassword.putString("stringKeyPassword", "not found!")
        editorSex.putString("stringKeySex", "not found!")
        editorFullName.putString("stringKeyFullName", "not found!")
        editorNameHelper.putString("stringKeyNameHelper", "not found!")
        editorPhoneHelper.putString("stringKeyPhoneHelper", "not found!")
        editorUserType.putString("stringKeyType", "not found!")

        editorID.apply()
        editorPhone.apply()
        editorPassword.apply()
        editorFullName.apply()
        editorNameHelper.apply()
        editorPhoneHelper.apply()
        editorUserType.apply()
        editorSex.apply()

        val intent = Intent(this, LoginScreen::class.java)
        startActivity(intent)
    }
}
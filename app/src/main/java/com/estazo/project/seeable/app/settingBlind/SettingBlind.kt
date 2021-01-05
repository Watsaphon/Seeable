package com.estazo.project.seeable.app.settingBlind

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import java.util.*
import android.content.res.Configuration
import android.view.LayoutInflater
import com.estazo.project.seeable.app.Login.LoginScreen
import com.estazo.project.seeable.app.MainActivity
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.SplashScreen
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class SettingBlind : AppCompatActivity() {

    private lateinit var backButton: ImageButton
    private lateinit var lang: Button
    private lateinit var account: Button
    private lateinit var setLocation: Button
    private lateinit var manageCare: Button
    private lateinit var logout: Button

    private lateinit var sharedPrefLanguage: SharedPreferences
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private lateinit var sharedPrefPhone: SharedPreferences
    private lateinit var sharedPrefPassword: SharedPreferences
    private lateinit var sharedPrefID: SharedPreferences
    private lateinit var sharedPrefDisplayName: SharedPreferences
    private lateinit var sharedPrefUserType : SharedPreferences
    private lateinit var sharedPrefCaretakerUser : SharedPreferences

    private lateinit var  mAlertDialog : AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_blind)

        backButton = findViewById(R.id.back_button)
        lang = findViewById(R.id.langBtn)
        account = findViewById(R.id.accountBtn)
        setLocation = findViewById(R.id.locationBtn)
        manageCare = findViewById(R.id.manageUserBtn)
        logout = findViewById(R.id.logoutBtn)

        sharedPrefLanguage = getSharedPreferences("value", 0)

        sharedPrefID = getSharedPreferences("value", 0)
        sharedPrefPhone= getSharedPreferences("value", 0)
        sharedPrefPassword= getSharedPreferences("value", 0)
        sharedPrefDisplayName= getSharedPreferences("value", 0)
        sharedPrefUserType = getSharedPreferences("value", 0)
        sharedPrefCaretakerUser = getSharedPreferences("value", 0)

        lang.setOnClickListener {
            changeLanguage()
        }
        account.setOnClickListener {
            account()
        }
        setLocation.setOnClickListener {
            searchLocation()
        }
        manageCare.setOnClickListener {
//            manageCaretaker()
            Toast.makeText(this@SettingBlind," manage Caretaker User",Toast.LENGTH_SHORT).show()
        }
        logout.setOnClickListener {
            gotoLogout()
        }
        backButton.setOnClickListener {
            onBackPressed()
        }

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
        val intent = Intent(this, SplashScreen::class.java)
        startActivity(intent)
    }

    private fun account(){
        val intent = Intent(this, AccountBlind::class.java)
        startActivity(intent)
    }

    private fun searchLocation(){
        val intent = Intent(this, SearchLocation::class.java)
        startActivity(intent)
    }

    private fun manageCaretaker(){

    }

    private fun gotoLogout(){

        val editorID = sharedPrefID.edit()
        val editorPhone = sharedPrefPhone.edit()
        val editorPassword = sharedPrefPassword.edit()
        val editorDisplay = sharedPrefDisplayName.edit()
        val editorUserType = sharedPrefUserType.edit()
        val editorCaretakerUser = sharedPrefCaretakerUser.edit()

        editorID.putString("stringKey2", "not found!")
        editorPhone.putString("stringKeyPhone", "not found!")
        editorPassword.putString("stringKeyPassword", "not found!")
        editorDisplay.putString("stringKeyDisplayName", "not found!")
        editorUserType.putString("stringKeyType", "not found!")
        editorCaretakerUser.putString("stringKeyCaretakerUser", "not found!")

        editorID.apply()
        editorPhone.apply()
        editorPassword.apply()
        editorDisplay.apply()
        editorUserType.apply()
        editorCaretakerUser.apply()

        val intent = Intent(this, LoginScreen::class.java)
        startActivity(intent)
    }


    /**receive value from realtime database (users_blind) and check Login */
    private var valueEventListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {

            dataSnapshot.ref.removeValue()

            val editorID = sharedPrefID.edit()
            val editorPhone = sharedPrefPhone.edit()
            val editorPassword = sharedPrefPassword.edit()
            val editorDisplay = sharedPrefDisplayName.edit()
            val editorUserType = sharedPrefUserType.edit()
            val editorCaretakerUser = sharedPrefCaretakerUser.edit()

            editorID.putString("stringKey2", "not found!")
            editorPhone.putString("stringKeyPhone", "not found!")
            editorPassword.putString("stringKeyPassword", "not found!")
            editorDisplay.putString("stringKeyDisplayName", "not found!")
            editorUserType.putString("stringKeyType", "not found!")
            editorCaretakerUser.putString("stringKeyCaretakerUser", "not found!")

            editorID.apply()
            editorPhone.apply()
            editorPassword.apply()
            editorDisplay.apply()
            editorUserType.apply()
            editorCaretakerUser.apply()

            dismissAlertDialogLoading()

            val intent = Intent(this@SettingBlind, LoginScreen::class.java)
            startActivity(intent)

        }
        override fun onCancelled(databaseError: DatabaseError) {}
    }

    /** AlertDialog to loading  */
    private fun alertDialogLoading() {
        //Inflate the dialog with custom view
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.loading_dialog, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
        //show dialog
        mAlertDialog  = mBuilder.show()
        mAlertDialog.window!!.setLayout(400,300)
        mAlertDialog.setCanceledOnTouchOutside(false)
        mAlertDialog.setCancelable(false)
    }

    /** AlertDialog to dismiss loading  */
    private fun dismissAlertDialogLoading() {
        //Inflate the dialog with custom view
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.loading_dialog, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
        //show dialog
        mAlertDialog.dismiss()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finishAffinity()
        Log.i("SettingBlind", "onBackPressed called")
    }

}
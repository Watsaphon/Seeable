 package com.estazo.project.seeable.app.caretaker


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.SplashScreen
import com.estazo.project.seeable.app.caretaker.settingCaretaker.BlindListViewModel
import com.estazo.project.seeable.app.login.LoginScreen
import java.util.*


 class MainActivityPerson : AppCompatActivity() {

    private lateinit var sharedPrefLanguage: SharedPreferences
    private lateinit var sharedPrefPhone: SharedPreferences
    private lateinit var sharedPrefPassword: SharedPreferences
    private lateinit var sharedPrefID: SharedPreferences
    private lateinit var sharedPrefDisplayName: SharedPreferences
    private lateinit var sharedPrefUserType : SharedPreferences
    private lateinit var sharedPrefBlindId : SharedPreferences

    private lateinit var language : String
    private lateinit var phone : String
    private lateinit var password : String
    private lateinit var id : String
    private lateinit var displayName : String
    private lateinit var userType: String
     private lateinit var currentBlindId : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_person)
        Log.i("MainActivityPerson", "onCreate called")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        hideSystemUI()

        sharedPrefLanguage = getSharedPreferences("value", 0)
        sharedPrefPhone= getSharedPreferences("value", 0)
        sharedPrefID = getSharedPreferences("value", 0)
        sharedPrefPassword= getSharedPreferences("value", 0)
        sharedPrefDisplayName= getSharedPreferences("value", 0)
        sharedPrefUserType = getSharedPreferences("value", 0)
        sharedPrefBlindId = getSharedPreferences("value", 0)

        language = sharedPrefLanguage.getString("stringKey", "not found!").toString()
        phone  = sharedPrefPhone.getString("stringKeyPhone", "not found!").toString()
        password  = sharedPrefPassword.getString("stringKeyPassword", "not found!").toString()
        id  = sharedPrefID.getString("stringKey2", "not found!").toString()
        displayName  = sharedPrefDisplayName.getString("stringKeyDisplayName", "not found!").toString()
        userType  = sharedPrefUserType.getString("stringKeyType", "not found!").toString()
        currentBlindId  = sharedPrefUserType.getString("stringKeyBlindId", "not found!").toString()

        Log.i("MainActivityPerson", "sharedPref -> Language : $language ," +
                "Phone : $phone , ID : $id, Password : $password," +
                " DisplayName : $displayName, UserType : $userType ")

        // Retrieve NavController from the NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navController: NavController = navHostFragment.navController

        // Set up the action bar for use with the NavController
//        setupActionBarWithNavController(navController)
//        val navController = this.findNavController(R.id.nav_host_fragment)
//        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
//        NavigationUI.setupActionBarWithNavController(this, navController)

//        val navController: NavController = Navigation.findNavController(this, R.id.nav_host)
//        appBarConfiguration = AppBarConfiguration.Builder().build()
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)


    }

    override fun onStart() {
        super.onStart()
        Log.i("MainActivityPerson", "onStart called")
    }
    override fun onResume() {
        super.onResume()
        Log.i("MainActivityPerson", "onResume called")
        updateUI()
    }
    override fun onPause() {
        super.onPause()
        Log.i("MainActivityPerson", "onPause called")
    }
    override fun onStop() {
        super.onStop()
        Log.i("MainActivityPerson", "onStop called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.i("MainActivityPerson", "onDestroy called")
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
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

        override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        hideSystemUI()
        Log.i("MainActivityPerson", "onWindowFocusChanged called")
    }

    /** change Language TH and EN*/
    fun changeLanguage(){
//        val language = sharedPrefLanguage.getString("stringKey", "not found!")
        Log.i("CheckLanguage", "Now Language is :$language")
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
        val intent = Intent(this, SplashScreen::class.java)
        startActivity(intent)
    }

    fun gotoLogout(){
        val editorID = sharedPrefID.edit()
        val editorPhone = sharedPrefPhone.edit()
        val editorPassword = sharedPrefPassword.edit()
        val editorDisplayName = sharedPrefDisplayName.edit()
        val editorUserType = sharedPrefUserType.edit()
        val editorBlindId = sharedPrefBlindId.edit()

        editorPhone.putString("stringKeyPhone", "not found!")
        editorPassword.putString("stringKeyPassword", "not found!")
        editorID.putString("stringKey2", "not found!")
        editorDisplayName.putString("stringKeyDisplayName","not found!")
        editorUserType.putString("stringKeyType", "not found!")
        editorBlindId.putString("stringKeyBlindId", "not found!")

        editorPhone.apply()
        editorPassword.apply()
        editorID.apply()
        editorUserType.apply()
        editorDisplayName.apply()
        editorBlindId.apply()

        val intent = Intent(this, LoginScreen::class.java)
        startActivity(intent)
    }

    private fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }



    fun updateUI() {
        val decorView = window.decorView
        decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            }
        }
    }



}

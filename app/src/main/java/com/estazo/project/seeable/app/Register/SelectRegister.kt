package com.estazo.project.seeable.app.Register


import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.estazo.project.seeable.app.AboutScreen
import com.estazo.project.seeable.app.Login.LoginScreen
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.SettingScreen
import com.estazo.project.seeable.app.SplashScreen
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.*


class SelectRegister : AppCompatActivity() {

    private lateinit var blindButton: Button
    private lateinit var personButton: Button
    private lateinit var blindGoogleButton: Button
    private lateinit var personGoogleButton: Button
    private lateinit var fab: FloatingActionButton
    private lateinit var sharedPrefLanguage: SharedPreferences
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var sharedPrefID: SharedPreferences
    private lateinit var sharedPrefGoogle : SharedPreferences
    private lateinit var sharedGooglePrefUserType : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_register)
        hideSystemUI()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        sharedPrefLanguage = getSharedPreferences("value", 0)
        sharedPrefID = getSharedPreferences("value", 0)
        sharedPrefGoogle  = getSharedPreferences("value", 0)
        sharedGooglePrefUserType = getSharedPreferences("value", 0)

        val userNormal = sharedPrefID.getString("stringKey2", "not found!")
        val userGoogle = sharedPrefGoogle.getString("stringKeyGoogle","not found!")

        val googleUserType = sharedGooglePrefUserType.getString("stringKeyGoogleType","not found!")

        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        Log.i("deejaa","$user")
        if (user != null) {
            val test = user.uid
            Log.i("deejaa","$test")
        }

        Log.i("deejaa","userNormal = $userNormal ,userGoogle = $userGoogle , GoogleuserType : $googleUserType")

        blindButton = findViewById(R.id.blinder_btn)
        personButton = findViewById(R.id.person_btn)
        blindGoogleButton = findViewById(R.id.blinderGoogle_btn)
        personGoogleButton = findViewById(R.id.personGoogle_btn)

        if(googleUserType == "noRegister"){
            blindGoogleButton.visibility = View.VISIBLE
            personGoogleButton.visibility = View.VISIBLE
        }
        else{
            blindButton.visibility = View.VISIBLE
            personButton.visibility = View.VISIBLE
        }

        fab = findViewById(R.id.floating_action_button)

        blindButton.setOnClickListener {
            val i = Intent(this@SelectRegister, RegisterBlind::class.java)
            startActivity(i)
        }
        personButton.setOnClickListener {
            val i = Intent(this@SelectRegister, RegisterPerson::class.java)
            startActivity(i)
        }
        blindGoogleButton.setOnClickListener {
            val i = Intent(this@SelectRegister, GoogleRegisterBlind::class.java)
            startActivity(i)
        }
        personGoogleButton.setOnClickListener {
            val i = Intent(this@SelectRegister, GoogleRegisterPerson::class.java)
            startActivity(i)
        }

        fab.setOnClickListener {
            /** PopupMenu dropdown */
            val popupMenu = PopupMenu(this, fab, Gravity.CENTER)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
            if(googleUserType == "noRegister" && userNormal == "not found!" ){
                popupMenu.menu.findItem(R.id.action_logout).isVisible = true
            }
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_about ->gotoAbout()
                    R.id.action_change_language -> changeLanguage()
                    R.id.action_settings -> gotoSetting()
                    R.id.action_logout -> gotoLogout()
                }
                hideSystemUI()
                true
            }
            popupMenu.show()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i("SelectRegister", "onStart called")
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    override fun onPause() {
        super.onPause()
        Log.i("SelectRegister", "onPause called")
    }

    override fun onStop() {
        super.onStop()

        Log.i("SelectRegister", "onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("SelectRegister", "onDestroy called")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val userNormal = sharedPrefID.getString("stringKey2", "not found!")
        val googleUserType = sharedGooglePrefUserType.getString("stringKeyGoogleType","not found!")
        if(googleUserType == "noRegister" && userNormal == "not found!" ){
            finishAffinity()
        }
        Log.i("SelectRegister", "onBackPressed called")
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        hideSystemUI()
    }




    private fun changeLanguage(){
        val language = sharedPrefLanguage.getString("stringKey", "not found!")
        Log.i("SelectRegister", "Now Language is :$language ")
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

    private fun gotoSetting(){
        val intent = Intent(this, SettingScreen::class.java)
        startActivity(intent)
    }

    private fun gotoAbout(){
        val intent = Intent(this, AboutScreen::class.java)
        startActivity(intent)
    }

    private fun gotoLogout(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if(acct != null){
            mGoogleSignInClient.signOut()
            Toast.makeText(this, getString(R.string.action_logout), Toast.LENGTH_SHORT).show()
        }

        FirebaseAuth.getInstance().signOut()

        var editorGoogleUser = sharedPrefGoogle.edit()
        var editorGoogleUserType = sharedPrefGoogle.edit()

        editorGoogleUser.putString("stringKeyGoogle", "not found!")
        editorGoogleUserType.putString("stringKeyGoogleType", "not found!")

        editorGoogleUser.apply()
        editorGoogleUserType.apply()

        val intent = Intent(this, LoginScreen::class.java)
        startActivity(intent)
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

    private fun updateUI() {
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

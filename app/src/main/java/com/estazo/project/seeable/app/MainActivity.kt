package com.estazo.project.seeable.app

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.estazo.project.seeable.app.login.LoginScreen
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var mGoogleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hideSystemUI()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        Log.i("MainActivity", "onCreate called ")

        val sharedPrefUserType = getSharedPreferences("value", 0)
        val userType = sharedPrefUserType.getString("stringKeyType","not found!")
        val editor3 = sharedPrefUserType.edit()

        Log.i("fff","userType : $userType")
        when (userType){
            "not found!" -> {
                // Retrieve NavController from the NavHostFragment
//                val navHostFragment = supportFragmentManager.findFragmentById(R.id.loginScreen) as NavHostFragment
////                view.findNavController().navigate(R.id.action_loginScreen_to_caretakerFragment)
//                val navController: NavController = navHostFragment.navController
                Toast.makeText(this, "null", Toast.LENGTH_LONG).show()
            }
            "blind" -> {
                // Retrieve NavController from the NavHostFragment
//                val navHostFragment = supportFragmentManager.findFragmentById(R.id.action_loginScreen_to_blindFragment) as NavHostFragment
////                view.findNavController().navigate(R.id.action_loginScreen_to_caretakerFragment)
//                val navController: NavController = navHostFragment.navController
                Toast.makeText(this, "blind", Toast.LENGTH_LONG).show()
            }
            "caretaker" -> {
                // Retrieve NavController from the NavHostFragment
//                val navHostFragment = supportFragmentManager.findFragmentById(R.id.caretakerFragment) as NavHostFragment
////                view.findNavController().navigate(R.id.action_loginScreen_to_caretakerFragment)
//                val navController: NavController = navHostFragment.navController
                Toast.makeText(this, "caretaker", Toast.LENGTH_LONG).show()
                 }
        }

//        // Retrieve NavController from the NavHostFragment
//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
//        val navController: NavController = navHostFragment.navController



    }

    override fun onStart() {
        super.onStart()
        Log.i("MainActivity", "onStart called")
    }

    override fun onResume() {
        super.onResume()
        Log.i("MainActivity", "onResume called")
        updateUI()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        hideSystemUI()
        Log.d("MainActivity", "onWindowFocusChanged called")
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

    fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


}
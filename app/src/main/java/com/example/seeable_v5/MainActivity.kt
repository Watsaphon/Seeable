package com.example.seeable_v5


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.transition.TransitionManager
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var button1: Button
    private lateinit var button2: Button
    private lateinit var button3: Button
    private lateinit var button4: Button
    private lateinit var fab: FloatingActionButton
    private lateinit var sharedPreferences: SharedPreferences



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hideSystemUI()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        sharedPreferences = getSharedPreferences("value", 0)
        var editor = sharedPreferences.edit()
        editor.putBoolean("FinishedInformation", true)
        editor.commit()


        val stringValue = sharedPreferences.getString("stringKey", "not found!")
        val booleanValue = sharedPreferences.getBoolean("FinishedInformation", false)

        Log.i("SplashScreenMain", "MainAc now language : $stringValue")
        Log.i("MainActivity ", "Boolean value: $booleanValue")

        fab = findViewById(R.id.floating_action_button)
        fab.setOnClickListener {
            /** PopupMenu dropdown */
            val popupMenu = PopupMenu(this, fab, Gravity.CENTER)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_about -> Toast.makeText(this, "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    R.id.action_change_language -> changeLanguage()
                    R.id.action_settings -> gotoSetting()
                }
                hideSystemUI()
                true
            }
            popupMenu.show()
        }

//        fab = findViewById(R.id.floating_action_button)
//        fab.setOnClickListener {
//
//            /** PopupWindow set gravity center */
//
//            val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//            // Inflate a custom view using layout inflater
//            val view = inflater.inflate(R.layout.another_view,null)
//
//            // Initialize a new instance of popup window
//            val popupWindow = PopupWindow(
//                view, // Custom view to show in popup window
//                LinearLayout.LayoutParams.WRAP_CONTENT, // Width of popup window
//                LinearLayout.LayoutParams.WRAP_CONTENT // Window height
//            )
//
//            // If API level 23 or higher then execute the code
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//                // Create a new slide animation for popup window enter transition
//                val slideIn = Slide()
//                slideIn.slideEdge = Gravity.TOP
//                popupWindow.enterTransition = slideIn
//
//                // Slide animation for popup window exit transition
//                val slideOut = Slide()
//                slideOut.slideEdge = Gravity.RIGHT
//                popupWindow.exitTransition = slideOut
//
//            }
//
//            // Get the widgets reference from custom view
//            val tv = view.findViewById<TextView>(R.id.text_view)
//            val buttonPopupX = view.findViewById<Button>(R.id.button_popup)
//            val buttonPopup1 = view.findViewById<Button>(R.id.button_popup1)
//            val buttonPopup2= view.findViewById<Button>(R.id.button_popup2)
//            val buttonPopup3 = view.findViewById<Button>(R.id.button_popup3)
//
//
//            // Set click listener for popup window's text view
//            tv.setOnClickListener{
//                // Change the text color of popup window's text view
//                tv.setTextColor(Color.RED)
//            }
//
//            // Set a click listener for popup's button widget
//            buttonPopupX.setOnClickListener{
//                // Dismiss the popup window
//                popupWindow.dismiss()
//            }
//            buttonPopup1.setOnClickListener{
//                Toast.makeText(applicationContext,"TEST1 Press!!",Toast.LENGTH_SHORT).show()
//            }
//            buttonPopup2.setOnClickListener{
//                Toast.makeText(applicationContext,"TEST2 Press!!",Toast.LENGTH_SHORT).show()
//            }
//            buttonPopup3.setOnClickListener{
//                Toast.makeText(applicationContext,"TEST3 Press!!",Toast.LENGTH_SHORT).show()
//            }
//            // Set a dismiss listener for popup window
//            popupWindow.setOnDismissListener {
//                Toast.makeText(applicationContext,"Popup closed",Toast.LENGTH_SHORT).show()
//            }
//
//            // Finally, show the popup window on app
//            TransitionManager.beginDelayedTransition(root_layout)
//            popupWindow.showAtLocation(
//                root_layout, // Location to display popup window
//                Gravity.CENTER, // Exact position of layout to display popup
//                0, // X offset
//                0 // Y offset
//            )
//
//        }

    }

    override fun onResume(){
        super.onResume()
        Log.i("MainActivity", "onResume called")
    }

    override fun onPause() {
        super.onPause()
        Log.i("MainActivity", "onPause called")
    }

    override fun onStop() {
        super.onStop()
        Log.i("MainActivity", "onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("MainActivity", "onDestroy called")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
        Log.i("MainActivity", "onBackPressed called")
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        hideSystemUI()
        Log.i("MainActivity", "onWindowFocusChanged called")
    }

    private fun changeLanguage(){
        val language = sharedPreferences.getString("stringKey", "not found!")
        Log.i("SplashScreenMain", "Now Language is :$language ")
        var locale: Locale? = null
        var editor = sharedPreferences.edit()
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

    private fun gotoSetting(){
        val intent = Intent(this,SettingScreen::class.java)
        startActivity(intent)

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

}




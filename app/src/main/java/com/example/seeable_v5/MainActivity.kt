package com.example.seeable_v5

import android.content.Context
import android.content.SharedPreferences
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
//        hideSystemUI()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        sharedPreferences = getSharedPreferences("first", 0)
        var editor = sharedPreferences.edit()
        editor.putBoolean("FinishedInformation", true)
        editor.apply()
//      editor.commit()
        Log.i("sharedPref_main", sharedPreferences.toString())

        fab = findViewById(R.id.floating_action_button)
        fab.setOnClickListener {

            /** PopupMenu dropdown */

//             Respond to FAB click
//             Toast.makeText(this, "FAB Work!!", Toast.LENGTH_SHORT).show()

            val popupMenu = PopupMenu(this, fab, Gravity.CENTER)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_crick -> Toast.makeText(
                        this@MainActivity,
                        "You Clicked : " + item.title,
                        Toast.LENGTH_SHORT
                    ).show()
                    R.id.action_ftbal -> Toast.makeText(
                        this@MainActivity,
                        "You Clicked : " + item.title,
                        Toast.LENGTH_SHORT
                    ).show()
                    R.id.action_hockey -> Toast.makeText(
                        this@MainActivity,
                        "You Clicked : " + item.title,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                hideSystemUI()
                true
            }
            popupMenu.show()

            /** PopupWindow set gravity center */

            val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            // Inflate a custom view using layout inflater
            val view = inflater.inflate(R.layout.another_view,null)

            // Initialize a new instance of popup window
            val popupWindow = PopupWindow(
                view, // Custom view to show in popup window
                LinearLayout.LayoutParams.WRAP_CONTENT, // Width of popup window
                LinearLayout.LayoutParams.WRAP_CONTENT // Window height
            )

            // If API level 23 or higher then execute the code
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                // Create a new slide animation for popup window enter transition
                val slideIn = Slide()
                slideIn.slideEdge = Gravity.TOP
                popupWindow.enterTransition = slideIn

                // Slide animation for popup window exit transition
                val slideOut = Slide()
                slideOut.slideEdge = Gravity.RIGHT
                popupWindow.exitTransition = slideOut

            }

            // Get the widgets reference from custom view
            val tv = view.findViewById<TextView>(R.id.text_view)
            val buttonPopupX = view.findViewById<Button>(R.id.button_popup)
            val buttonPopup1 = view.findViewById<Button>(R.id.button_popup1)
            val buttonPopup2= view.findViewById<Button>(R.id.button_popup2)
            val buttonPopup3 = view.findViewById<Button>(R.id.button_popup3)


            // Set click listener for popup window's text view
            tv.setOnClickListener{
                // Change the text color of popup window's text view
                tv.setTextColor(Color.RED)
            }

            // Set a click listener for popup's button widget
            buttonPopupX.setOnClickListener{
                // Dismiss the popup window
                popupWindow.dismiss()
            }
            buttonPopup1.setOnClickListener{
                Toast.makeText(applicationContext,"TEST1 Press!!",Toast.LENGTH_SHORT).show()
            }
            buttonPopup2.setOnClickListener{
                Toast.makeText(applicationContext,"TEST2 Press!!",Toast.LENGTH_SHORT).show()
            }
            buttonPopup3.setOnClickListener{
                Toast.makeText(applicationContext,"TEST3 Press!!",Toast.LENGTH_SHORT).show()
            }
            // Set a dismiss listener for popup window
            popupWindow.setOnDismissListener {
                Toast.makeText(applicationContext,"Popup closed",Toast.LENGTH_SHORT).show()
            }

            // Finally, show the popup window on app
            TransitionManager.beginDelayedTransition(root_layout)
            popupWindow.showAtLocation(
                root_layout, // Location to display popup window
                Gravity.CENTER, // Exact position of layout to display popup
                0, // X offset
                0 // Y offset
            )

        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
        Log.i("Main Activity", "onBackPressed called")
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        hideSystemUI()
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
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                )
    }
}
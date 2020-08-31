package com.example.seeable_v5

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import com.google.android.material.floatingactionbutton.FloatingActionButton


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
            // Respond to FAB click
            // Toast.makeText(this, "FAB Work!!", Toast.LENGTH_SHORT).show()

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





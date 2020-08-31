package com.example.seeable_v5

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class SelectRegister : AppCompatActivity() {
    private lateinit var blindButton: Button
    private lateinit var personButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_register)
        hideSystemUI()
        blindButton = findViewById(R.id.blinder_btn)
        personButton = findViewById(R.id.person_btn)

        blindButton.setOnClickListener {
            val i = Intent(this@SelectRegister, RegisterBlind::class.java)
            startActivity(i)
        }
        personButton.setOnClickListener {
            val i = Intent(this@SelectRegister, RegisterPerson::class.java)
            startActivity(i)
        }
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
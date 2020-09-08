package com.estazo.project.seeable.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText

class RegisterBlind : AppCompatActivity() {

    private lateinit var nameBox : EditText
    private lateinit var surnameBox : EditText
    private lateinit var phoneBox : EditText
    private lateinit var helperBox : EditText
    private lateinit var phoneHelperBox : EditText
    private lateinit var finishButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_blind)
        Log.i("RegisterBlind", "onCreate called")
        hideSystemUI()
        nameBox = findViewById(R.id.name_box)
        surnameBox = findViewById(R.id.surname_box)
        phoneBox = findViewById(R.id.phone_box)
        helperBox = findViewById(R.id.helper_box)
        phoneHelperBox = findViewById(R.id.phone_helper_box)
        finishButton = findViewById(R.id.regis_finish_button)

        finishButton.setOnClickListener {

            val inputName = nameBox.text
            val inputSurname = surnameBox.text
            val inputPhone = phoneBox.text
            val inputHelper = helperBox.text
            val inputPhoneHelper = phoneHelperBox.text

            if(inputName.isEmpty()&&inputSurname.isEmpty()&&inputPhone.isEmpty()&&inputHelper.isEmpty()&&inputPhoneHelper.isEmpty()){
                nameBox.error = getString(R.string.name_box_blind)
                surnameBox.error = getString(R.string.surname_box_blind)
                phoneBox.error = getString(R.string.phone_box_blind)
                helperBox.error = getString(R.string.helper_box_blind)
                phoneHelperBox.error = getString(R.string.phoneHelper_box_blind)
            }
            else if(inputName.isEmpty()){
                nameBox.error = getString(R.string.name_box_blind)
            }
            else if(inputSurname.isEmpty()){
                surnameBox.error = getString(R.string.surname_box_blind)
            }
            else if(inputPhone.isEmpty()){
                phoneBox.error = getString(R.string.phone_box_blind)
            }
            else if(inputHelper.isEmpty())
            {
                helperBox.error = getString(R.string.helper_box_blind)
            }
            else if(inputPhoneHelper.isEmpty())
            {
                phoneHelperBox.error = getString(R.string.phoneHelper_box_blind)
            }
            else{
                val i = Intent(this@RegisterBlind, MainActivity::class.java)
                startActivity(i)
                finish()
            }
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
        Log.i("RegisterBlind", "hideSystemUI called")
    }
}
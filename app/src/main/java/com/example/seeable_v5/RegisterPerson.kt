package com.example.seeable_v5

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class RegisterPerson : AppCompatActivity() {

    private lateinit var nameBox : EditText
    private lateinit var surnameBox : EditText
    private lateinit var phoneBox : EditText
    private lateinit var finishButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_person )
        hideSystemUI()
        nameBox = findViewById(R.id.name_box)
        surnameBox = findViewById(R.id.surname_box)
        phoneBox = findViewById(R.id.phone_box)
        finishButton = findViewById(R.id.regis_finish_button)

        finishButton.setOnClickListener {
            val inputName = nameBox.text
            val inputSurname = surnameBox.text
            val inputPhone = phoneBox.text
            if(inputName.isEmpty()&&inputSurname.isEmpty()&&inputPhone.isEmpty()){
                nameBox.error = "Please insert person's name."
                surnameBox.error = "Please insert Person's surname."
                phoneBox.error = "Please insert Person's phone."
            }
            else if(inputName.isEmpty()){
                nameBox.error = "Please insert person's name."
            }
            else if(inputSurname.isEmpty()){
                surnameBox.error = "Please insert person's surname."
            }
            else if(inputPhone.isEmpty()){
                phoneBox.error = "Please insert person's phone."
            }
            else{
                val i = Intent(this@RegisterPerson, MainActivity::class.java)
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
    }
}


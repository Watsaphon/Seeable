package com.estazo.project.seeable.app.Register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.estazo.project.seeable.app.HelperClass.UserPersonHelperClass
import com.estazo.project.seeable.app.Login.LoginScreen
import com.estazo.project.seeable.app.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.FirebaseDatabase

class RegisterPerson : AppCompatActivity() {

    private lateinit var userName : TextInputEditText
    private lateinit var password : TextInputEditText
    private lateinit var fullName : TextInputEditText
    private lateinit var phoneBox : TextInputEditText

    private lateinit var finishButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_person)
        Log.i("RegisterPerson", "onCreate called")
        hideSystemUI()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        userName = findViewById(R.id.username_box)
        password = findViewById(R.id.password_box)
        fullName = findViewById(R.id.fullname_box)
        phoneBox = findViewById(R.id.phone_box)
        finishButton = findViewById(R.id.regis_finish_button)

        finishButton.setOnClickListener {
            val inputUsername: String = userName.text.toString()
            val inputPassword: String = password.text.toString()
            val inputfullName: String = fullName.text.toString()
            val inputPhone: String = phoneBox.text.toString()


            if(inputUsername.isEmpty()&&inputPassword.isEmpty()&&inputfullName.isEmpty()&&inputPhone.isEmpty()){
                userName.error =  getString(R.string.username_box_person)
                password.error = getString(R.string.password_box_person)
                fullName.error = getString(R.string.fullname_box_person)
                phoneBox.error = getString(R.string.phone_box_person)
            }
            else if(inputUsername.isEmpty()){
                password.error = getString(R.string.username_box_person)
            }
            else if(inputPassword.isEmpty()){
                password.error = getString(R.string.password_box_person)
            }
            else if(inputfullName.isEmpty()){
                userName.error =  getString(R.string.fullname_box_person)
            }
            else if(inputPhone.isEmpty()){
                phoneBox.error = getString(R.string.phone_box_person)
            }
            else{
                val ref = FirebaseDatabase.getInstance().getReference("users_person")
                val ID = ref.push().key
                val test =
                    UserPersonHelperClass(
                        ID.toString(),
                        inputUsername,
                        inputPassword,
                        inputfullName,
                        inputPhone
                    )
                ref.child(ID.toString()).setValue(test).addOnCompleteListener {
                    Toast.makeText(this,getString(R.string.success_regis), Toast.LENGTH_SHORT).show()
                }
                saveRegister()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        hideSystemUI()
        Log.i("RegisterPerson", "onWindowFocusChanged called")
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

    private fun saveRegister(){
        val i = Intent(this, LoginScreen::class.java)
        startActivity(i)
        finish()
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
}


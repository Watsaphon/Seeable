package com.estazo.project.seeable.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthResult
import com.google.firebase.database.FirebaseDatabase


class RegisterBlind : AppCompatActivity() {

    private lateinit var userName : TextInputEditText
    private lateinit var password : TextInputEditText
    private lateinit var fullName : TextInputEditText
    private lateinit var phoneBox : TextInputEditText
    private lateinit var helperBox : TextInputEditText
    private lateinit var phoneHelperBox : TextInputEditText
    private lateinit var finishButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_blind)
        Log.i("RegisterBlind", "onCreate called")
        hideSystemUI()

        userName = findViewById(R.id.username_box)
        password = findViewById(R.id.password_box)
        fullName = findViewById(R.id.fullname_box)
        phoneBox = findViewById(R.id.phone_box)
        helperBox = findViewById(R.id.helper_box)
        phoneHelperBox = findViewById(R.id.phone_helper_box)
        finishButton = findViewById(R.id.regis_finish_button)



        finishButton.setOnClickListener {
        val inputUsername: String = userName.text.toString()
        val inputPassword: String = password.text.toString()
        val inputfullName: String = fullName.text.toString()
        val inputPhone: String = phoneBox.text.toString()
        val inputHelper: String = helperBox.text.toString()
        val inputPhoneHelper: String = phoneHelperBox.text.toString()


            if(inputUsername.isEmpty()&&inputPassword.isEmpty()&&inputfullName.isEmpty()&&inputPhone.isEmpty()&&inputHelper.isEmpty()&&inputPhoneHelper.isEmpty()){
                userName.error = getString(R.string.username_box_blind)
                password.error = getString(R.string.password_box_blind)
                fullName.error = getString(R.string.fullname_box_blind)
                phoneBox.error = getString(R.string.phone_box_blind)
                helperBox.error = getString(R.string.helper_box_blind)
                phoneHelperBox.error = getString(R.string.phoneHelper_box_blind)
            }
            else if(inputUsername.isEmpty()){
                userName.error = getString(R.string.username_box_blind)
            }
            else if(inputPassword.isEmpty()){
                userName.error = getString(R.string.password_box_blind)
            }
            else if(inputfullName.isEmpty()){
                fullName.error = getString(R.string.fullname_box_blind)
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
                val ref = FirebaseDatabase.getInstance().getReference("users_blind")
                val testID = ref.push().key
                val test = UserBlinderHelperClass(inputUsername,inputPassword , inputfullName, inputPhone, inputHelper, inputPhoneHelper)
                ref.child(testID.toString()).setValue(test).addOnCompleteListener {
                    Toast.makeText(this,"Successfully Save Database",Toast.LENGTH_SHORT).show()
                }
                saveRegister()

            }
        }


    }

    private fun saveRegister(){
    val i = Intent(this@RegisterBlind, SelectRegister::class.java)
    startActivity(i)
    finish()
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
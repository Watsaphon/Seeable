package com.estazo.project.seeable.app.Register

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import com.estazo.project.seeable.app.HelperClass.UserBlinderHelperClass
import com.estazo.project.seeable.app.Login.LoginScreen
import com.estazo.project.seeable.app.MainActivity
import com.estazo.project.seeable.app.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class GoogleRegisterBlind : AppCompatActivity() {

    private lateinit var fullName : TextInputEditText
    private lateinit var phoneBox : TextInputEditText
    private lateinit var helperBox : TextInputEditText
    private lateinit var phoneHelperBox : TextInputEditText
    private lateinit var finishButton: Button

    private lateinit var sharedPrefGoogle : SharedPreferences
    private lateinit var sharedPrefUserType: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_register_blind)
        Log.i("GoogleRegisterBlind", "onCreate called")
        hideSystemUI()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        sharedPrefGoogle  = getSharedPreferences("value", 0)
        sharedPrefUserType = getSharedPreferences("value", 0)

        fullName = findViewById(R.id.fullname_box)
        phoneBox = findViewById(R.id.phone_box)
        helperBox = findViewById(R.id.helper_box)
        phoneHelperBox = findViewById(R.id.phone_helper_box)
        finishButton = findViewById(R.id.regis_finish_button)

        finishButton.setOnClickListener {
            val inputfullName: String = fullName.text.toString()
            val inputPhone: String = phoneBox.text.toString()
            val inputHelper: String = helperBox.text.toString()
            val inputPhoneHelper: String = phoneHelperBox.text.toString()


            if(inputfullName.isEmpty()&&inputPhone.isEmpty()&&inputHelper.isEmpty()&&inputPhoneHelper.isEmpty()){
                fullName.error = getString(R.string.fullname_box_blind)
                phoneBox.error = getString(R.string.phone_box_blind)
                helperBox.error = getString(R.string.helper_box_blind)
                phoneHelperBox.error = getString(R.string.phoneHelper_box_blind)
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
                val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
                var UID  = ""
                if (user != null) {
                     UID = user.uid
                }
                val ID = ref.push().key
            Log.i("comeiei"," ID : $ID , UID : $UID")
                val test = UserBlinderHelperClass(
                        UID,
                        "-",
                        "-",
                        inputfullName,
                        inputPhone,
                        inputHelper,
                        inputPhoneHelper,
                        13.0000021,
                        22.0000001
                    )
                ref.child(UID).setValue(test).addOnCompleteListener {
                    Toast.makeText(this,getString(R.string.success_regis), Toast.LENGTH_SHORT).show()
                }
                var editorGoogleUser = sharedPrefGoogle.edit()
                var editorUserType = sharedPrefUserType.edit()

                editorGoogleUser.putString("stringKeyGoogle","$UID")
                editorUserType.putString("stringKeyType", "blind")

                editorGoogleUser.apply()
                editorUserType.apply()

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
        Log.i("RegisterBlind", "onWindowFocusChanged called")
    }

    private fun saveRegister(){
        val i = Intent(this, MainActivity::class.java)
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
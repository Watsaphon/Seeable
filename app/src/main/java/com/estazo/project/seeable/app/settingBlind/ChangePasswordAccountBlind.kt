package com.estazo.project.seeable.app.settingBlind

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.estazo.project.seeable.app.R
import com.google.firebase.database.FirebaseDatabase

class ChangePasswordAccountBlind : AppCompatActivity() {

    private lateinit var back : View
    private lateinit var editCurrentPassword : EditText
    private lateinit var editNewPassword : EditText
    private lateinit var save : Button
    private lateinit var sharedPrefPhone: SharedPreferences
    private lateinit var sharedPrefPassword: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password_account_blind)

        back = findViewById(R.id.back_button)
        editCurrentPassword = findViewById(R.id.editCurrentPassword)
        editNewPassword = findViewById(R.id.editNewPassword)
        save = findViewById(R.id.saveChangePassword)
        sharedPrefPhone = getSharedPreferences("value", 0)
        sharedPrefPassword = getSharedPreferences("value", 0)


        back.setOnClickListener{
            onBackPressed()
        }

        save.setOnClickListener{
            saveChangePassword()
        }


    }

    private fun saveChangePassword() {
        val currentPhone = sharedPrefPhone.getString("stringKeyPhone", "not found!")
        val password = sharedPrefPassword.getString("stringKeyPassword","not found!")
        val editCurrent = editCurrentPassword.text.toString()
        val editNew = editNewPassword.text.toString()

        Log.d("savePassword","phone :$currentPhone , password : $password , editCurrent :$editCurrent , editNew :$editNew ")

        if( password == editCurrent && password != editNew && editCurrent != editNew && editNew.isNotBlank() && editCurrent.isNotBlank() ){

            val ref = FirebaseDatabase.getInstance().reference
            ref.child("users_blind/$currentPhone/password").setValue("$editNew")

            val editor = sharedPrefPassword.edit()
            editor.putString("stringKeyPassword","$editNew")
            editor.apply()

            finish()
//            val intent = Intent(this, AccountBlind::class.java)
//            startActivity(intent)
        }
        else if(editCurrent.isBlank()){
            Toast.makeText(this,R.string.checkNull_current_ChangePassword, Toast.LENGTH_SHORT).show()
        }
        else if( editNew.isBlank()){
            Toast.makeText(this,R.string.checkNull_new_ChangePassword, Toast.LENGTH_SHORT).show()
        }
        else if( password != editCurrent){
            Toast.makeText(this,R.string.check_current_ChangePassword, Toast.LENGTH_SHORT).show()
        }
        else if( editNew == editCurrent){
            Toast.makeText(this,R.string.check_new_ChangePassword, Toast.LENGTH_SHORT).show()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
//        val intent = Intent(this, AccountBlind::class.java)
//        startActivity(intent)
        finish()
        Log.i("EditNameAccountBlind", "onBackPressed called")
    }
}
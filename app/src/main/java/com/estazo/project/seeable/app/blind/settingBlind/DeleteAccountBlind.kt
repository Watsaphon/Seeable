package com.estazo.project.seeable.app.blind.settingBlind

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.estazo.project.seeable.app.login.LoginScreen
import com.estazo.project.seeable.app.R
import com.google.firebase.database.FirebaseDatabase

class DeleteAccountBlind : AppCompatActivity() {

    private lateinit var back : View
    private lateinit var editPassword : EditText
    private lateinit var delete : Button

    private lateinit var sharedPrefPhone: SharedPreferences
    private lateinit var sharedPrefPassword: SharedPreferences
    private lateinit var sharedPrefID: SharedPreferences
    private lateinit var sharedPrefDisplayName: SharedPreferences
    private lateinit var sharedPrefUserType : SharedPreferences
    private lateinit var sharedPrefCaretakerUser : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_account_blind)

        back = findViewById(R.id.back_button)
        editPassword = findViewById(R.id.editPassword)
        delete = findViewById(R.id.delete)

        sharedPrefID = getSharedPreferences("value", 0)
        sharedPrefPhone= getSharedPreferences("value", 0)
        sharedPrefPassword= getSharedPreferences("value", 0)
        sharedPrefDisplayName= getSharedPreferences("value", 0)
        sharedPrefUserType = getSharedPreferences("value", 0)
        sharedPrefCaretakerUser = getSharedPreferences("value", 0)

        back.setOnClickListener{
            onBackPressed()
        }
        delete.setOnClickListener{
          deleteAccount()
        }
    }

    private fun deleteAccount(){
        val currentPhone = sharedPrefPhone.getString("stringKeyPhone", "not found!")
        val password = sharedPrefPassword.getString("stringKeyPassword","not found!")
        val confirmPassword = editPassword.text.toString()

        Log.d("deleteAccount"," password : $password , confirmPassword :$confirmPassword ")

        when {
            password == confirmPassword -> {
                val ref = FirebaseDatabase.getInstance().reference
                ref.child("users_blind/$currentPhone").removeValue()

                val editorPhone = sharedPrefPhone.edit()
                val editorPassword = sharedPrefPassword.edit()
                val editorID = sharedPrefID.edit()
                val editorDisplay = sharedPrefDisplayName.edit()
                val editorUserType = sharedPrefUserType.edit()
                val editorCaretakerUser = sharedPrefCaretakerUser.edit()

                editorID.putString("stringKey2", "not found!")
                editorPhone.putString("stringKeyPhone", "not found!")
                editorPassword.putString("stringKeyPassword", "not found!")
                editorDisplay.putString("stringKeyDisplayName", "not found!")
                editorUserType.putString("stringKeyType", "not found!")
                editorCaretakerUser.putString("stringKeyCaretakerUser", "not found!")

                editorID.apply()
                editorPhone.apply()
                editorPassword.apply()
                editorDisplay.apply()
                editorUserType.apply()
                editorCaretakerUser.apply()

                val intent = Intent(this@DeleteAccountBlind, LoginScreen::class.java)
                startActivity(intent)

            }
            confirmPassword.isEmpty() -> {
                Toast.makeText(this,R.string.checkNull_current_ChangePassword, Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this,R.string.checkPassword_DeleteAccountBlind, Toast.LENGTH_SHORT).show()
            }
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
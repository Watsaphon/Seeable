package com.estazo.project.seeable.app.settingBlind

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spanned
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.estazo.project.seeable.app.R

class AccountBlind : AppCompatActivity() {

    private lateinit var back : View
    private lateinit var displayName : TextView
    private lateinit var phone : TextView
    private lateinit var editName : Button
    private lateinit var changePass : Button
    private lateinit var deleteUser : Button
    private lateinit var sharedPrefLanguage: SharedPreferences
    private lateinit var sharedPrefPhone: SharedPreferences
    private lateinit var sharedPrefDisplayName: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_blind)

        back = findViewById(R.id.back_button)
        displayName = findViewById(R.id.displayName)
        phone = findViewById(R.id.phone)
        editName = findViewById(R.id.editName)
        changePass = findViewById(R.id.changePassword)
        deleteUser = findViewById(R.id.deleteUser)

        sharedPrefLanguage = getSharedPreferences("value", 0)
        sharedPrefPhone = getSharedPreferences("value", 0)
        sharedPrefDisplayName = getSharedPreferences("value", 0)

        val currentPhone = sharedPrefPhone.getString("stringKeyPhone", "not found!")
        val currentDisplay = sharedPrefDisplayName.getString("stringKeyDisplayName","not found!")

        val language = sharedPrefLanguage.getString("stringKey", "not found!")
        if(language == "en"){
            phone.text = "Phone : $currentPhone"
            displayName.text = currentDisplay
        }
        else if(language =="th"){
            phone.text = "เบอร์โทรศัพท์ : $currentPhone"
            displayName.text = currentDisplay
        }

        Log.d("accountB","currentPhone :$currentPhone , currentDisplay : $currentDisplay")

        back.setOnClickListener{
            onBackPressed()
        }
        editName.setOnClickListener{
            val intent = Intent(this,EditNameAccountBlind::class.java)
            startActivity(intent)
        }
        changePass.setOnClickListener{
            val intent = Intent(this,ChangePasswordAccountBlind::class.java)
            startActivity(intent)
        }
        deleteUser.setOnClickListener{
            val intent = Intent(this,DeleteAccountBlind::class.java)
            startActivity(intent)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, SettingBlind::class.java)
        startActivity(intent)
        finishAffinity()
        Log.i("AccountBlind", "onBackPressed called")
    }

}
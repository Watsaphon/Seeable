package com.estazo.project.seeable.app.settingBlind

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.estazo.project.seeable.app.HelperClass.UserBlinderHelperClassNew
import com.estazo.project.seeable.app.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class EditNameAccountBlind : AppCompatActivity() {

    private lateinit var back : View
    private lateinit var editName : EditText
    private lateinit var phone : TextView
    private lateinit var save : Button
    private lateinit var sharedPrefLanguage: SharedPreferences
    private lateinit var sharedPrefPhone: SharedPreferences
    private lateinit var sharedPrefDisplayName: SharedPreferences

    var newName = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_name_account_blind)

        back = findViewById(R.id.back_button)
        editName = findViewById(R.id.editDisplayName)
        phone = findViewById(R.id.phone)
        save = findViewById(R.id.saveNewName)



        sharedPrefLanguage = getSharedPreferences("value", 0)
        sharedPrefPhone = getSharedPreferences("value", 0)
        sharedPrefDisplayName = getSharedPreferences("value", 0)

        val currentPhone = sharedPrefPhone.getString("stringKeyPhone", "not found!")
        val currentDisplay = sharedPrefDisplayName.getString("stringKeyDisplayName","not found!")
        val language = sharedPrefLanguage.getString("stringKey", "not found!")

        if(language == "en"){
            phone.text = "Phone : $currentPhone"
        }
        else if(language =="th"){
            phone.text = "เบอร์โทรศัพท์ : $currentPhone"
        }
        back.setOnClickListener{
            onBackPressed()
        }
        save.setOnClickListener{
            newName = editName.text.toString()
            if(newName == currentDisplay ){
                Toast.makeText(this, R.string.check_same_editDisplayName, Toast.LENGTH_SHORT).show()
            }
            else if(newName == "" ){
                Toast.makeText(this, R.string.check_null_editDisplayName, Toast.LENGTH_SHORT).show()
            }
            else{

                val ref = FirebaseDatabase.getInstance().reference
                ref.child("users_blind/$currentPhone/displayName").setValue("$newName")


                val editor = sharedPrefDisplayName.edit()
                editor.putString("stringKeyDisplayName","$newName")
                editor.apply()

                val intent = Intent(this, AccountBlind::class.java)
                startActivity(intent)
            }

        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, AccountBlind::class.java)
        startActivity(intent)
        finishAffinity()
        Log.i("EditNameAccountBlind", "onBackPressed called")
    }

}